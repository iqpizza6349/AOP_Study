package com.example.aop_study.service;

import com.example.aop_study.global.aop.annotation.ExecTimer;
import com.example.aop_study.library.domain.entity.Book;
import com.example.aop_study.library.domain.repository.BookRepository;
import com.example.aop_study.library.dto.BookResponseDto;
import com.example.aop_study.library.service.BookServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class BookServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImpl bookService;

    @BeforeAll
    void defaultData() {
        for (int i = 0; i < 10; i++) {
            saveData(String.valueOf(i));
        }
    }

    @AfterAll
    void after() {
        bookRepository.deleteAll();
    }

    private void saveData(String title) {
        bookRepository.save(Book.builder()
                .title(title)
                .author("작가")
                .publisher("출판사")
                .build());
    }

    @Test
    public void 도서_조회() {
        PageRequest pageRequest = bookService.setPageRequest("asc", "title");
        List<BookResponseDto> responses = bookService.findAllByTitle("1", pageRequest);

        assertThat(responses.size()).isSameAs(1);
    }

    @DisplayName("전체 도서를 조회합니다.")
    @Test
    public void 도서_전체_조회() {
        PageRequest pageRequest = bookService.setPageRequest("asc", "title");
        List<BookResponseDto> responses = bookService.findAllByAuthor("작가", pageRequest);

        assertThat(responses.size()).isSameAs(10);
    }

}
