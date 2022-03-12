package com.example.aop_study.global.exception;

import com.example.aop_study.global.response.dto.ExceptionResponse;
import com.example.aop_study.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdviceHandler {

    private final ResponseService responseService;

    @ExceptionHandler(value = HttpClientErrorException.class)
    protected ResponseEntity<ExceptionResponse> commonException(HttpClientErrorException e) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timeStamp(new Date())
                .message(e.getStatusText())
                .build();

        return responseService.getResponse(exceptionResponse, e.getStatusCode());
    }

}