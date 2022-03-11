package com.example.aop_study.library.domain.repository;

import com.example.aop_study.library.domain.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatronRepository extends JpaRepository<Patron, Long> {

    Optional<Patron> findByName(String name);
    
}
