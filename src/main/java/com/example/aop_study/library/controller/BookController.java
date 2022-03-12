package com.example.aop_study.library.controller;

import com.example.aop_study.library.dto.BookRequestDto;
import com.example.aop_study.library.dto.BookResponseDto;
import com.example.aop_study.library.service.BookServiceImpl;
import com.example.aop_study.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> searchByKeyword(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String patron,
            @RequestParam String page
    ) {
        PageRequest pageRequest = bookService.setPageRequest(page, "title");

        if (keyword != null && author != null && patron != null) {
            return responseService.getResponse(bookService.findAllByAll(keyword, author, patron, pageRequest));
        }
        else if (keyword != null && author != null) {
            return responseService.getResponse(bookService.findAllByTitleAndAuthor(keyword, author, pageRequest));
        }
        else if (keyword != null && patron != null) {
            return responseService.getResponse(bookService.findAllByTitleAndPatron(keyword, patron, pageRequest));
        }
        else if (author != null && patron != null) {
            return responseService.getResponse(bookService.findAllByAuthorAndPatron(author, patron, pageRequest));
        }
        else if (keyword != null) {
            return responseService.getResponse(bookService.findAllByTitle(keyword, pageRequest));
        }
        else if (author != null) {
            return responseService.getResponse(bookService.findAllByAuthor(author, pageRequest));
        }
        else if (patron != null) {
            return responseService.getResponse(bookService.findAllByPatron(patron, pageRequest));
        }
        else {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "keyword, author, patron 중 한 가지는 충족되어야합니다.");
        }
    }

}
