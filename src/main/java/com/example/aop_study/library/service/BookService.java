package com.example.aop_study.library.service;

import com.example.aop_study.library.domain.entity.Book;
import com.example.aop_study.library.dto.BookRequestDto;
import com.example.aop_study.library.dto.BookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BookService {

    BookResponseDto register(String token, BookRequestDto bookRequestDto);

    List<BookResponseDto> findAllByTitle(String keyword, PageRequest pageRequest);

    List<BookResponseDto> findAllByAuthor(String authorName, PageRequest pageRequest);

    List<BookResponseDto> findAllByPatron(String patronName, PageRequest pageRequest);

    List<BookResponseDto> findAllByTitleAndAuthor(String keyword, String authorName, PageRequest pageRequest);

    List<BookResponseDto> findAllByTitleAndPatron(String keyword, String patronName, PageRequest pageRequest);

    List<BookResponseDto> findAllByAuthorAndPatron(String authorName, String patronName, PageRequest pageRequest);

    List<BookResponseDto> findAllByAll(String keyword, String authorName, String patronName, PageRequest pageRequest);

    boolean isLoaned(Long id);

    boolean isDamaged(Long id);

    boolean loan(BookRequestDto bookRequestDto);

    PageRequest setPageRequest(String request, String type);
}
