package com.example.aop_study.library.service;

import com.example.aop_study.library.domain.entity.Book;
import com.example.aop_study.library.domain.repository.BookRepository;
import com.example.aop_study.library.dto.BookRequestDto;
import com.example.aop_study.library.dto.BookResponseDto;
import com.example.aop_study.token.config.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final TokenProvider tokenProvider;
    private final PatronServiceImpl patronService;

    @Override
    public BookResponseDto register(String token, BookRequestDto bookRequestDto) {
        String title = bookRequestDto.getTitle();
        String author = bookRequestDto.getAuthor();

        if (bookRepository.existsByTitleAndAuthor(title, author)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "이미 동일한 책이 존재합니다.");
        }

        if ((bookRequestDto.getPatron() == null || bookRequestDto.getPatron().isBlank())
                && token.isBlank()) {
            // 후원자도 없고, 관리자 권한 역시 없다면 403
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "책을 등록할 수 있는 권한이 없습니다.");
        }

        boolean isDonated = !bookRequestDto.getPatron().isBlank();

        if (!isDonated && !tokenProvider.checkManager(token)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "책을 등록할 수 있는 권한이 없습니다.");
        }

        return new BookResponseDto(save(bookRequestDto, isDonated));
    }

    Book save(BookRequestDto bookRequestDto, boolean isDonated) {
        Book book = Book.builder()
                .title(bookRequestDto.getTitle())
                .author(bookRequestDto.getAuthor())
                .publisher(bookRequestDto.getPublisher())
                .build();

        if (isDonated) {
            book.setPatron(patronService.findByName(bookRequestDto.getPatron()));
        }

        return bookRepository.save(book);
    }

    @Override
    public List<BookResponseDto> findAllByTitle(String keyword) {
        return null;
    }

    @Override
    public List<BookResponseDto> findAllByAuthor(String authorName) {
        return null;
    }

    @Override
    public List<BookResponseDto> findAllByPatron(String patronName) {
        return null;
    }

    @Override
    public boolean isLoaned(Long id) {
        return false;
    }

    @Override
    public boolean isDamaged(Long id) {
        return false;
    }

    @Override
    public boolean loan(BookRequestDto bookRequestDto) {
        return false;
    }

}
