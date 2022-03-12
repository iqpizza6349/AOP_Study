package com.example.aop_study.global.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RetryAfterResponse {

    @JsonProperty(value = "Retry-After")
    private String RetryAfter;

}
