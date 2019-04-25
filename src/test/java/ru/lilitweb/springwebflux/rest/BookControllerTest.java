package ru.lilitweb.springwebflux.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import ru.lilitweb.springwebflux.domain.Author;
import ru.lilitweb.springwebflux.domain.Book;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.assertj.core.api.Assertions;
import reactor.core.publisher.Mono;
import ru.lilitweb.springwebflux.domain.Genre;

import java.util.Collections;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testCreate() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));

        webTestClient.post().uri("/book")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.title").isEqualTo("Руслан и Людмила");
    }

    @Test
    public void testUpdate() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));

        book.setGenres(Collections.singletonList(new Genre("some genre")));
        mongoTemplate.save(book);

        Book newBook = new Book(
                "new title",
                2020,
                "new desc",
                new Author("new author"));

        webTestClient.put()
                .uri("/book/{id}", Collections.singletonMap("id", book.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newBook), Book.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.title").isEqualTo("new title")
                .jsonPath("$.description").isEqualTo("new desc")
                .jsonPath("$.author.fullname").isEqualTo("new author")
                .jsonPath("$.year").isEqualTo("2020");
    }

    @Test
    public void testDelete() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));

        book.setGenres(Collections.singletonList(new Genre("some genre")));
        mongoTemplate.save(book);

        webTestClient.delete()
                .uri("/book/{id}", Collections.singletonMap("id",  book.getId()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testListBooks() {
        webTestClient.get().uri("/books")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Book.class);
    }
    @Test
    public void testView() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));

        book.setGenres(Collections.singletonList(new Genre("some genre")));
        mongoTemplate.save(book);

        webTestClient.get()
                .uri("/book/{id}", Collections.singletonMap("id", book.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

}
