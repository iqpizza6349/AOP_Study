package com.example.aop_study.library.domain.repository;

import com.example.aop_study.library.domain.entity.Book;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByTitleAndAuthor(String title, String author);

    @Query("select b from Book b where b.title like concat('%', ?1, '%')")
    List<Book> findAllByTitleContains(String title, PageRequest pageRequest);

    @Query("select b from Book b where b.author like concat('%', ?1, '%')")
    List<Book> findAllByAuthorContains(String author, PageRequest pageRequest);

    @Query("select b from Book b where b.patron.name like concat('%', ?1, '%')")
    List<Book> findAllByPatronName(String patron_name, PageRequest pageRequest);

    @Query("select b from Book b where b.title like concat(?1, '%') and b.author like concat('%', ?1, '%')")
    List<Book> findAllByTitleAndAuthor(String title, String author, PageRequest pageRequest);

    @Query("select b from Book b where b.title like concat(?1, '%') and b.patron.name like concat('%', ?1, '%')")
    List<Book> findAllByTitleAndPatron(String title, String patron_name, PageRequest pageRequest);

    @Query("select b from Book b where b.author like concat(?1, '%') and b.patron.name like concat('%', ?1, '%')")
    List<Book> findAllByAuthorAndPatron(String title, String author, PageRequest pageRequest);

    @Query("select b from Book b where b.title like concat(?1, '%') and b.author like concat(?2, '%') and b.patron.name like concat('%', ?1, '%')")
    List<Book> findAllByTitleAndAuthorAndPatron(String title, String author, String patron_name, PageRequest pageRequest);

}
