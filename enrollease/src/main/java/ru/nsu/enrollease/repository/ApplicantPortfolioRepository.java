package ru.nsu.enrollease.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.enrollease.model.ApplicantPortfolio;

@Repository
public interface ApplicantPortfolioRepository extends MongoRepository<ApplicantPortfolio, String> {
//    @Query("{ 'ФизическоеЛицоСНИЛС': ?0 }")
//    boolean existsByPrimaryKey(String primaryKey);

//    @Query("{ 'ФизическоеЛицоСНИЛС': ?0 }")
    boolean existsByIian(String iian);

    ApplicantPortfolio findByIian(String iian);

    ApplicantPortfolio deleteByIian(String primaryKey);
}