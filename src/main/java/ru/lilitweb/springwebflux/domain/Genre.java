package ru.lilitweb.springwebflux.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Genre{
    @NonNull
    private String name;
}
