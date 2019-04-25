package ru.lilitweb.springwebflux.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.lilitweb.springwebflux.domain.Author;
import ru.lilitweb.springwebflux.domain.Book;
import ru.lilitweb.springwebflux.domain.Genre;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository repository;
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void add() {
        Mono<Book> bookMono = repository.save(new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author")));

        StepVerifier
                .create(bookMono)
                .assertNext(book -> assertNotNull(book.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    public void update() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));

        book.setGenres(Collections.singletonList(new Genre("some genre")));
        mongoTemplate.save(book);
        book = repository.findById(book.getId()).block();
        assertNotNull(book);

        book.setTitle("new title");
        book.setDescription("new description");
        book.setAuthor(new Author("new author"));
        book.setGenres(Arrays.asList(new Genre("some genre1"), new Genre("some genre2")));
        Mono<Book> bookMono = repository.save(book);

        StepVerifier
                .create(bookMono)
                .assertNext(b -> {
                    assertNotNull(b.getId());
                    assertEquals("new title", b.getTitle());
                    assertEquals("new description", b.getDescription());
                    assertEquals("new author", b.getAuthor().getFullname());
                })
                .expectComplete()
                .verify();
    }
}
