package ru.nsu.enrollease.repository;

import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.nsu.enrollease.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    // You can define custom queries here if needed
    User findByEmail(@NonNull String email);

}
