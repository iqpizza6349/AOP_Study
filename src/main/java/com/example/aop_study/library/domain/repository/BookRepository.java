package com.example.aop_study.library.domain.repository;

import com.example.aop_study.library.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByTitleAndAuthor(String title, String author);

    @Query("select b from Book b where b.title like concat(?1, '%')")
    List<Book> findAllByTitleContains(String title);

    @Query("select b from Book b where b.author = ?1")
    List<Book> findAllByAuthor(String author);

    @Query("select b from Book b where b.patron.name = ?1")
    List<Book> findAllByPatronName(String patron_name);

}
