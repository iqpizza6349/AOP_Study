package com.example.aop_study.library.service;

import com.example.aop_study.library.domain.entity.Patron;
import com.example.aop_study.library.domain.repository.PatronRepository;
import com.example.aop_study.library.dto.PatronDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Transactional
@RequiredArgsConstructor
@Service
public class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;

    @Transactional(readOnly = true)
    @Override
    public Patron findByName(String name) {
        return patronRepository.findByName(name)
                .orElseThrow(
                        () -> new HttpClientErrorException(
                                HttpStatus.NOT_FOUND, "해당 이름을 가진 후원자를 찾지 못했습니다."
                        )
                );
    }

    @Override
    public PatronDto registerPatron(String name) {
        if (patronRepository.existsByName(name)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "이미 존재하는 이름의 후원자가 존재합니다.");
        }

        Patron patron = Patron.builder()
                .name(name)
                .build();
        return new PatronDto(patronRepository.save(patron));
    }

    @Override
    public void deletePatron(String name) {
        if (!patronRepository.existsByName(name)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "해당 이름을 가진 후원자를 찾지 못했습니다.");
        }

        patronRepository.delete(findByName(name));
    }
}
