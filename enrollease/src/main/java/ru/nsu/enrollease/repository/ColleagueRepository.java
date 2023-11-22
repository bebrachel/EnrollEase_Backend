package ru.nsu.enrollease.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.nsu.enrollease.model.Colleague;

public interface ColleagueRepository extends MongoRepository<Colleague, String> {

    // You can define custom queries here if needed
    Colleague findByEmail(String email);

    boolean existsByEmail(String email);
}
