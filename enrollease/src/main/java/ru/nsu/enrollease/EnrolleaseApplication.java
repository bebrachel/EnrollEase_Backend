package ru.nsu.enrollease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "ru.nsu.enrollease.repository")
public class EnrolleaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrolleaseApplication.class, args);
    }

}
