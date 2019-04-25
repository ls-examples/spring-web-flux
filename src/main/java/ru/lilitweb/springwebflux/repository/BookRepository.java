package ru.lilitweb.springwebflux.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lilitweb.springwebflux.domain.Author;
import ru.lilitweb.springwebflux.domain.Book;
import ru.lilitweb.springwebflux.domain.Genre;

import java.util.List;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findByGenresIn(List<Genre> genres);
    Flux<Book> findByAuthor(Author author);

    Flux<Book> findAll(Sort sort);
    Flux<Book> findByTitleContainsOrAuthorFullnameContains(String title, String author, Pageable pageable);
}
