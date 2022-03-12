package com.example.aop_study.library.service;

import com.example.aop_study.library.domain.entity.Book;
import com.example.aop_study.library.dto.BookRequestDto;
import com.example.aop_study.library.dto.BookResponseDto;

import java.util.List;

public interface BookService {

    BookResponseDto register(String token, BookRequestDto bookRequestDto);

    List<BookResponseDto> findAllByTitle(String keyword);

    List<BookResponseDto> findAllByAuthor(String authorName);

    List<BookResponseDto> findAllByPatron(String patronName);

    boolean isLoaned(Long id);

    boolean isDamaged(Long id);

    boolean loan(BookRequestDto bookRequestDto);

}
