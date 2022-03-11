package com.example.aop_study.library.service;

import com.example.aop_study.library.domain.entity.Patron;

public interface PatronService {

    Patron findByName(String name);

}
