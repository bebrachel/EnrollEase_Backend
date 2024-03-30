package ru.nsu.enrollease.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.enrollease.model.RolePrivilege;

@Repository
public interface RolePrivilegeRepository extends MongoRepository<RolePrivilege, String> {

    Optional<RolePrivilege> findByName(String name);

}