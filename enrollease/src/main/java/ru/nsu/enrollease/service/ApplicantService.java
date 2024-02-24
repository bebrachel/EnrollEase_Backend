package ru.nsu.enrollease.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.model.Applicant;
import ru.nsu.enrollease.repository.ApplicantRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class ApplicantService {

    private final ApplicantRepository applicantRepository;

    @Transactional
    public List<Applicant> getAllApplicants() {
        return applicantRepository.findAll();
    }

    @Transactional
    public boolean isExists(@NonNull String iian) {
        return applicantRepository.existsByIian(iian);
    }

    @Transactional
    public Applicant createNewApplicant(@NonNull Applicant applicant) {
        if (isExists(applicant.getIian())) {
            throw new IllegalArgumentException("Applicant with such iian is already created.");
        }
        log.info(applicant + "Was added");
        return applicantRepository.save(applicant);
    }

}
