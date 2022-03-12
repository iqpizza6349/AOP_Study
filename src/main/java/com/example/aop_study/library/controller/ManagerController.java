package com.example.aop_study.library.controller;

import com.example.aop_study.global.response.service.ResponseService;
import com.example.aop_study.library.dto.BookIdDto;
import com.example.aop_study.library.dto.BookRequestDto;
import com.example.aop_study.library.dto.BookResponseDto;
import com.example.aop_study.library.service.BookServiceImpl;
import com.example.aop_study.library.service.ManagerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RequestMapping("/api/managers")
@RequiredArgsConstructor
@RestController
public class ManagerController {

    private final ManagerServiceImpl managerService;
    private final BookServiceImpl bookService;
    private final ResponseService responseService;

    // 책 추가
    @PostMapping("/books")
    public ResponseEntity<BookResponseDto> registerBook(
            @RequestHeader(name = "X-AUTH-TOKEN") String token,
            @RequestBody BookRequestDto bookRequestDto) {
        if (!managerService.checkToken(token)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "관리자 정보가 없는 토큰입니다.");
        }

        BookResponseDto bookResponseDto = bookService.registerBook(token, bookRequestDto);
        return responseService.getResponse(bookResponseDto, HttpStatus.CREATED);
    }

    // 책 폐기
    @DeleteMapping("/books")
    public ResponseEntity<?> deleteBook(
            @RequestHeader(name = "X-AUTH-TOKEN") String token,
            @RequestBody BookIdDto bookIdDto) {
        if (!managerService.checkToken(token)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "관리자 정보가 없는 토큰입니다.");
        }

        bookService.deleteBook(bookIdDto.getId());
        return responseService.getCommonResponse(HttpStatus.NO_CONTENT);
    }

}
