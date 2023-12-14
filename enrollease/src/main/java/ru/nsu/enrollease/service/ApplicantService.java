package ru.nsu.enrollease.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.model.Applicant;
import ru.nsu.enrollease.repository.ApplicantRepository;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;

    @Transactional
    public List<Applicant> getAllApplicants() {
        return applicantRepository.findAll();
    }

    @Transactional
    public boolean isExists(@NonNull String iian) {
        return applicantRepository.existsByФизическоеЛицоСНИЛС(iian);
    }

}
