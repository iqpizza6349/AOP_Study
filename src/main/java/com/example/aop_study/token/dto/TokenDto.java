package com.example.aop_study.token.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TokenDto {

    private String grantType;
    private String accessToken;
    private Long accessTokenExpireDate;

}
