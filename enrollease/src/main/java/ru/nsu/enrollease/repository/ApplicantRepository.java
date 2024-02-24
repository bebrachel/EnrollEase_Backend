package ru.nsu.enrollease.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.enrollease.model.Applicant;

@Repository
public interface ApplicantRepository extends MongoRepository<Applicant, String> {

    //    ApplicantRepository findByEmail(String email);
//
    @Query("{ iian: ?0 }")
    boolean existsByPrimaryKey(String fieldValue);

    boolean existsByIian(String value);
}