package ru.nsu.enrollease.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.model.User;
import ru.nsu.enrollease.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createOrUpdate(@NonNull String email, @NonNull List<String> roles) {
        var user = userRepository.findByEmail(email);
        if (user != null) {
            user.setRoles(roles);
        } else {
            user = new User(email, roles);
        }
        return userRepository.save(user);
    }
}
