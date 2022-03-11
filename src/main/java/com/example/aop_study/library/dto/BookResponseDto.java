package com.example.aop_study.library.dto;

import com.example.aop_study.library.domain.entity.Book;
import lombok.Getter;

@Getter
public class BookResponseDto {

    private final Long id;
    private final String title;
    private final String author;
    private final String publisher;
    private final boolean loaned;
    private final boolean damaged;
    private String patron;

    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.loaned = book.isLoaned();
        this.damaged = book.isDamaged();

        if (book.getPatron() != null) {
            this.patron = book.getPatron().getName();
        }
    }

}
