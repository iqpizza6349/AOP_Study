package com.example.aop_study.token.controller;

import com.example.aop_study.global.response.service.ResponseService;
import com.example.aop_study.library.service.ManagerServiceImpl;
import com.example.aop_study.token.dto.TokenDto;
import com.example.aop_study.token.dto.TokenRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/token")
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final ManagerServiceImpl managerService;
    private final ResponseService responseService;

    // 토큰 발행
    @PostMapping
    public ResponseEntity<TokenDto> issueToken(@RequestBody TokenRequestDto tokenRequestDto) {
        TokenDto tokenDto = managerService.issueToken(tokenRequestDto.getManagerId());
        return responseService.getResponse(tokenDto, HttpStatus.CREATED);
    }

}
