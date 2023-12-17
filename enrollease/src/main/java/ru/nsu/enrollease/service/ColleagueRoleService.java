package ru.nsu.enrollease.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.enrollease.model.ColleagueRole;
import ru.nsu.enrollease.model.RolePrivilege;
import ru.nsu.enrollease.repository.ColleagueRoleRepository;

@Service
@RequiredArgsConstructor
public class ColleagueRoleService {

    private final ColleagueRoleRepository colleagueRoleRepository;

    public ColleagueRole getRole(@NonNull String name) {
        return colleagueRoleRepository.findByName(name).orElseThrow();
    }

    public ColleagueRole createNewRole(@NonNull String name,
        @NonNull List<RolePrivilege> privilegeList) {
        getRole(name);
        return colleagueRoleRepository.save(new ColleagueRole(name, privilegeList));
    }
}
