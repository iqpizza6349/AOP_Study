package com.example.aop_study.library.service;

import com.example.aop_study.token.dto.TokenDto;

public interface ManagerService {

    TokenDto issueToken(String managerId);

    boolean checkToken(String token);

}
