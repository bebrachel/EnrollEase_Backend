package ru.nsu.enrollease.service;

import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.enrollease.repository.ApplicantRepository;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    public void takeOrUpdateDocuments(@NonNull String iian, @NonNull Map<String, String> data){

    }
}
