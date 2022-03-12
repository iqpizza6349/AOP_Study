package com.example.aop_study.library.service;

import com.example.aop_study.library.domain.entity.Book;
import com.example.aop_study.library.domain.repository.BookRepository;
import com.example.aop_study.library.dto.BookRequestDto;
import com.example.aop_study.library.dto.BookResponseDto;
import com.example.aop_study.token.config.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final TokenProvider tokenProvider;
    private final PatronServiceImpl patronService;

    @Override
    public BookResponseDto registerBook(String token, BookRequestDto bookRequestDto) {
        String title = bookRequestDto.getTitle();
        String author = bookRequestDto.getAuthor();

        if (bookRepository.existsByTitleAndAuthor(title, author)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "이미 동일한 책이 존재합니다.");
        }

        if ((bookRequestDto.getPatron() == null || bookRequestDto.getPatron().isBlank())
                && (token == null || token.isBlank())) {
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
    @Transactional(readOnly = true)
    public List<BookResponseDto> findAllByTitle(String keyword, PageRequest pageRequest) {
        List<Book> books = bookRepository.findAllByTitleContains(keyword, pageRequest);
        return books.stream().map(BookResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> findAllByAuthor(String authorName, PageRequest pageRequest) {
        List<Book> books = bookRepository.findAllByAuthorContains(authorName, pageRequest);
        return books.stream().map(BookResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> findAllByPatron(String patronName, PageRequest pageRequest) {
        List<Book> books = bookRepository.findAllByPatronName(patronName, pageRequest);
        return books.stream().map(BookResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponseDto> findAllByTitleAndAuthor(String keyword, String authorName, PageRequest pageRequest) {
        List<Book> books = bookRepository.findAllByTitleAndAuthor(keyword, authorName, pageRequest);
        return books.stream().map(BookResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponseDto> findAllByTitleAndPatron(String keyword, String patronName, PageRequest pageRequest) {
        List<Book> books = bookRepository.findAllByTitleAndPatron(keyword, patronName, pageRequest);
        return books.stream().map(BookResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponseDto> findAllByAuthorAndPatron(String authorName, String patronName, PageRequest pageRequest) {
        List<Book> books = bookRepository.findAllByAuthorAndPatron(authorName, patronName, pageRequest);
        return books.stream().map(BookResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponseDto> findAllByAll(String keyword, String authorName, String patronName, PageRequest pageRequest) {
        List<Book> books = bookRepository.findAllByTitleAndAuthorAndPatron(keyword, authorName, patronName, pageRequest);
        return books.stream().map(BookResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDto loanBook(Long id) {
        Book book = findById(id);
        if (book.isLoaned()) {
             // 이미 대출되었다면
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "이미 대출된 도서입니다.");
        }
        if (book.isDamaged()) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "파손된 도서는 대출할 수 없습니다.");
        }

        book.updateLoaned(true);
        return new BookResponseDto(book);
    }

    @Override
    public BookResponseDto returnBook(Long id) {
        Book book = findById(id);
        if (!book.isLoaned()) {
            // 이미 대출되었다면
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "대출되지않은 도서입니다.");
        }

        book.updateLoaned(false);
        return new BookResponseDto(book);
    }

    @Transactional(readOnly = true)
    Book findById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(
                    () -> new HttpClientErrorException(HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public PageRequest setPageRequest(String request, String type) {
        if (request == null || request.isBlank()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "`page`(param) must be acs or desc");
        }
        request = request.toLowerCase();

        if (request.equals("acs")) {
            return PageRequest.of(0, 10, Sort.by(type).ascending());
        }

        return PageRequest.of(0, 10, Sort.by(type).descending());
    }

    @Override
    public void deleteBook(Long id) {
        Book book = findById(id);
        
        if (book.isLoaned()) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "대출 상태인 도서는 삭제할 수 없습니다.");
        }
        if (!book.isDamaged()) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "이상 없는 도서를 삭제할 수 없습니다.");
        }

        bookRepository.delete(book);
    }
}
