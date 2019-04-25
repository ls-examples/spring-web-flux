package ru.lilitweb.springwebflux.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "ru.lilitweb.springwebflux.repository")
@EnableMongoAuditing
public class MongoConfig {
}
