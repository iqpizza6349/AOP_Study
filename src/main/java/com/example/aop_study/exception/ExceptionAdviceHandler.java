package com.example.aop_study.exception;

import com.example.aop_study.response.dto.CommonResult;
import com.example.aop_study.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdviceHandler {

    private final ResponseService responseService;

    @ExceptionHandler(value = HttpClientErrorException.class)
    protected CommonResult commonException(HttpClientErrorException e) {
        return responseService.getFailResult(e.getStatusText());
    }

}
