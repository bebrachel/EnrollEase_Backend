package ru.nsu.enrollease.repository;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.enrollease.model.Colleague;

@Repository
public interface ColleagueRepository extends MongoRepository<Colleague, String> {

    // You can define custom queries here if needed
    Optional<Colleague> findByEmail(String email);

    void deleteById(@NonNull String email);
}
