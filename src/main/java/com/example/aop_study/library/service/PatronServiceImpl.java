package com.example.aop_study.library.service;

import com.example.aop_study.library.domain.entity.Patron;
import com.example.aop_study.library.domain.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
@Service
public class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;

    @Override
    public Patron findByName(String name) {
        return patronRepository.findByName(name)
                .orElseThrow(
                        () -> new HttpClientErrorException(
                                HttpStatus.NOT_FOUND, "해당 이름을 가진 후원자를 찾지 못했습니다."
                        )
                );
    }
}
