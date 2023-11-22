package ru.nsu.enrollease.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.model.Colleague;
import ru.nsu.enrollease.repository.ColleagueRepository;

@Service
@RequiredArgsConstructor
public class ColleagueService {

//    private final MongoTemplate mongoTemplate;
    private final ColleagueRepository colleagueRepository;

    @Transactional
    public Colleague allowOrGiveRoles(@NonNull String email, @NonNull List<String> roles) {
        var user = colleagueRepository.findByEmail(email);
        if (user != null) {
            user.setRoles(roles);
        } else {
            user = new Colleague(email, roles);
        }
        return colleagueRepository.save(user);
    }

    @Transactional
    public boolean isAllowed(@NonNull String email) {
        return colleagueRepository.existsByEmail(email);
    }
}
