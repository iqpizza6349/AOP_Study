package com.example.aop_study.library.service;

import com.example.aop_study.library.domain.entity.Patron;
import com.example.aop_study.library.dto.PatronDto;

public interface PatronService {

    Patron findByName(String name);

    PatronDto registerPatron(String name);

    void deletePatron(String name);

}
