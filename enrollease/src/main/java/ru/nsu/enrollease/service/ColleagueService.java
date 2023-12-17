package ru.nsu.enrollease.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.model.Colleague;
import ru.nsu.enrollease.model.ColleagueRole;
import ru.nsu.enrollease.repository.ColleagueRepository;

@Service
@RequiredArgsConstructor
public class ColleagueService {

    private final ColleagueRepository colleagueRepository;

    @Transactional
    public Colleague allowOrGiveRoles(@NonNull String email, @NonNull List<ColleagueRole> roles) {
        var user = colleagueRepository.findByEmail(email).orElse(new Colleague());
        user.setEmail(email);
        user.setRoles(roles);
        user.setEnabled(true);
        return colleagueRepository.save(user);
    }

    @Transactional
    public boolean isAllowed(@NonNull String email) {
        var user = colleagueRepository.findByEmail(email);
        return user.map(Colleague::isEnabled).orElse(false);
    }

    public List<Colleague> getAllColleagues() {
        return colleagueRepository.findAll();
    }

    public void deleteColleagueByEmail(@NonNull String email) {
        colleagueRepository.deleteById(email);
    }
}
