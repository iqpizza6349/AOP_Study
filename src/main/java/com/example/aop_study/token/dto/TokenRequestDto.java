package com.example.aop_study.token.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenRequestDto {

    @JsonProperty(value = "manager_id")
    private String managerId;

}
