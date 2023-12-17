package ru.nsu.enrollease.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.enrollease.model.ColleagueRole;

@Repository
public interface ColleagueRoleRepository extends MongoRepository<ColleagueRole, String> {

    Optional<ColleagueRole> findByName(String name);

}