package com.example.aop_study.library.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    private boolean loaned = false;

    private boolean damaged = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Patron patron;

    public void updateLoaned(boolean loaned) {
        this.loaned = loaned;
    }

    public void updateDamaged() {
        this.damaged = true;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

}
