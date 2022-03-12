package com.example.aop_study.library.controller;

import com.example.aop_study.global.aop.annotation.ExecTimer;
import com.example.aop_study.library.dto.BookIdDto;
import com.example.aop_study.library.dto.BookRequestDto;
import com.example.aop_study.library.dto.BookResponseDto;
import com.example.aop_study.library.service.BookServiceImpl;
import com.example.aop_study.global.response.service.ResponseService;
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

    @ExecTimer
    @PostMapping
    public ResponseEntity<BookResponseDto> donateBook(@RequestBody BookRequestDto bookRequestDto) {
        return responseService.getResponse(bookService.registerBook(null, bookRequestDto), HttpStatus.CREATED);
    }

    @ExecTimer
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> searchByKeyword(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String patron,
            @RequestParam(defaultValue = "acs") String page
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
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "keyword, author, patron 중 한 가지는 충족되어야합니다.");
        }
    }

    // 대출
    @ExecTimer
    @PatchMapping("loan")
    public ResponseEntity<BookResponseDto> loanByTitle(@RequestBody BookIdDto bookIdDto) {
        if (bookIdDto.getId() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "`id`(body) must be Number");
        }

        return responseService.getResponse(bookService.loanBook(bookIdDto.getId()), HttpStatus.CREATED);
    }

    // 반납
    @ExecTimer
    @PatchMapping("return")
    public ResponseEntity<BookResponseDto> returnByTitle(@RequestBody BookIdDto bookIdDto) {
        if (bookIdDto.getId() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "`id`(body) must be Number");
        }

        return responseService.getResponse(bookService.returnBook(bookIdDto.getId()), HttpStatus.CREATED);
    }

}
