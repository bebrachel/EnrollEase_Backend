package ru.nsu.enrollease.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.nsu.enrollease.model.Applicant;

public interface ApplicantRepository extends MongoRepository<Applicant, String> {

//    ApplicantRepository findByEmail(String email);
//
//    boolean existsByEmail(String email);
}