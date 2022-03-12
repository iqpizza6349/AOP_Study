package com.example.aop_study.library.service;

import com.example.aop_study.token.config.TokenProvider;
import com.example.aop_study.token.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final TokenProvider tokenProvider;

    @Override
    public TokenDto issueToken(String managerId) {
        return tokenProvider.createToken(managerId);
    }

    @Override
    public boolean checkToken(String token) {
        return tokenProvider.checkManager(token);
    }
}
