package com.example.aop_study.library.domain.repository;

import com.example.aop_study.library.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByTitleAndAuthor(String title, String author);

}
