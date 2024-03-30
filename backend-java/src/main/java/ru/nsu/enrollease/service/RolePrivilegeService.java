package ru.nsu.enrollease.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.enrollease.model.RolePrivilege;
import ru.nsu.enrollease.repository.RolePrivilegeRepository;

@Service
@RequiredArgsConstructor
public class RolePrivilegeService {

    private final RolePrivilegeRepository rolePrivilegeRepository;

    public RolePrivilege getPrivilege(@NonNull String name) {
        return rolePrivilegeRepository.findByName(name).orElseThrow();
    }

    public RolePrivilege createNewPrivilege(@NonNull String name) {
        getPrivilege(name);
        return rolePrivilegeRepository.save(new RolePrivilege(name));
    }
}
