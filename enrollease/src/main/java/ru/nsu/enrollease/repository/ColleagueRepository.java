package ru.nsu.enrollease.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.enrollease.model.Applicant;
import ru.nsu.enrollease.model.Colleague;

public interface ColleagueRepository extends MongoRepository<Colleague, String> {

    // You can define custom queries here if needed
    Colleague findByEmail(String email);

    boolean existsByEmail(String email);

}
