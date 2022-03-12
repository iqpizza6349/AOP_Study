package com.example.aop_study.library.controller;

import com.example.aop_study.library.dto.BookRequestDto;
import com.example.aop_study.library.dto.BookResponseDto;
import com.example.aop_study.library.service.BookServiceImpl;
import com.example.aop_study.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/books")
@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookServiceImpl bookService;
    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<BookResponseDto> donateBook(@RequestBody BookRequestDto bookRequestDto) {
        return responseService.getResponse(bookService.register(null, bookRequestDto), HttpStatus.CREATED);
    }

}
