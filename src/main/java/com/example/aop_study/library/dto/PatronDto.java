package com.example.aop_study.library.dto;

import com.example.aop_study.library.domain.entity.Patron;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PatronDto {

    private String name;

    public PatronDto(Patron patron) {
        this.name = patron.getName();
    }
}
