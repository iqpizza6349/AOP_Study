package com.example.aop_study.controller;

import com.example.aop_study.library.domain.entity.Patron;
import com.example.aop_study.library.domain.repository.PatronRepository;
import com.example.aop_study.library.dto.BookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private ObjectMapper objectMapper;

    @BeforeAll
    void before() {
        Patron patron = Patron.builder()
                .name("워크샵6349")
                .build();
        patronRepository.save(patron);
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

}
