package com.example.aop_study.controller;

import com.example.aop_study.library.domain.entity.Book;
import com.example.aop_study.library.domain.entity.Patron;
import com.example.aop_study.library.domain.repository.BookRepository;
import com.example.aop_study.library.domain.repository.PatronRepository;
import com.example.aop_study.library.dto.BookRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void before() {
        Patron patron = Patron.builder()
                .name("워크샵6349")
                .build();
        patronRepository.save(patron);
    }

    @AfterAll
    void after() {
        patronRepository.deleteAll();
    }

    @Test
    public void 도서_기부_성공() throws Exception {
        String title = "도서 제목";
        String author = "작가";
        String publisher = "워크샵 출판사";
        String patron = "워크샵6349";
        BookRequestDto bookRequestDto = new BookRequestDto(title, author, publisher);
        bookRequestDto.setPatron(patron);

        mockMvc.perform(
                post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", title).exists())
                .andExpect(jsonPath("$.author", author).exists())
                .andExpect(jsonPath("$.publisher", publisher).exists())
                .andExpect(jsonPath("$.patron", patron).exists())
                .andDo(print());
    }

    @DisplayName("이미 등록된 도서가 기부가 되지않는 지 검증")
    @Test
    public void 도서_기부_실패1() throws Exception {
        Book book = Book.builder()
                .title("매너가 사람을 만든다.")
                .author("Harry Hart")
                .publisher("킹스맨")
                .build();
        bookRepository.save(book);

        BookRequestDto bookRequestDto = new BookRequestDto("매너가 사람을 만든다.", "Harry Hart", "킹스맨");
        bookRequestDto.setPatron("워크샵6349");

        mockMvc.perform(
                        post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookRequestDto))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @DisplayName("미등록 후원자로 기부가 되지않는 지 검증")
    @Test
    public void 도서_기부_실패2() throws Exception {
        BookRequestDto bookRequestDto = new BookRequestDto("매너가 사람을 만든다.", "Harry Hart", "킹스맨");
        bookRequestDto.setPatron("심심한 대장장이");

        mockMvc.perform(
                        post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookRequestDto))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
