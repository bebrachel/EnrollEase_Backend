package ru.nsu.enrollease.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.nsu.enrollease.repository.UserRepository;
import ru.nsu.enrollease.service.UserService;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    private final UserRepository userRepository;
}
