package ru.lilitweb.springwebflux.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lilitweb.springwebflux.domain.Book;
import ru.lilitweb.springwebflux.repository.BookRepository;

@RestController
public class BookController {

    private BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/books")
    public Flux<Book> all() {
        return repository.findAll();
    }

    @GetMapping("/book/{id}")
    public Mono<ResponseEntity<Book>> view(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/book")
    public Mono<ResponseEntity<Book>> create(@RequestBody Book book) {
        return repository.save(book)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/book/{id}")
    public Mono<ResponseEntity<Book>> update(@PathVariable String id, @RequestBody Book book) {
        return repository.findById(id)
                .flatMap((b) -> {
                    book.setId(id);
                    return repository.save(book)
                            .map(ResponseEntity::ok)
                            .defaultIfEmpty(ResponseEntity.notFound().build());
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/book/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return repository.deleteById(id);
    }
}
