package ru.lilitweb.springwebflux.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @NonNull
    private String title;

    @NonNull
    private int year;

    @NonNull
    private String description;

    @NonNull
    private Author author;

    @CreatedDate
    private Date createdAt;

    private List<Genre> genres = new ArrayList<>();
}
