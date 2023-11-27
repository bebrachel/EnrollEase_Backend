package ru.nsu.enrollease.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.enrollease.model.Applicant;

@Repository
public interface ApplicantRepository extends MongoRepository<Applicant, String> {

    //    ApplicantRepository findByEmail(String email);
//
    @Query("{ 'ФизическоеЛицоСНИЛС': ?0 }")
    boolean existsByPrimaryKey(String fieldValue);

    boolean existsByФизическоеЛицоСНИЛС(String value);
}