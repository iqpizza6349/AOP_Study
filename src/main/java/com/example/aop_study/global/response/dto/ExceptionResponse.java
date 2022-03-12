package com.example.aop_study.global.response.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ExceptionResponse {
    
    private final Date timeStamp;
    private final String message;

}
