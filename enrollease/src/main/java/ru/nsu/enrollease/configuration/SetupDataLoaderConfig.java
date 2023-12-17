package ru.nsu.enrollease.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.model.ColleagueRole;
import ru.nsu.enrollease.model.RolePrivilege;
import ru.nsu.enrollease.repository.ColleagueRoleRepository;
import ru.nsu.enrollease.repository.RolePrivilegeRepository;
import ru.nsu.enrollease.service.ColleagueService;

@Component
@RequiredArgsConstructor
public class SetupDataLoaderConfig implements
    ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final ColleagueService colleagueRepository;

    private final ColleagueRoleRepository colleagueRoleRepository;

    private final RolePrivilegeRepository rolePrivilegeRepository;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        RolePrivilege readPrivilege
            = createPrivilegeIfNotFound("READ_PRIVILEGE");
        RolePrivilege writePrivilege
            = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<RolePrivilege> adminPrivileges = Arrays.asList(
            readPrivilege, writePrivilege);
        createRoleIfNotFound("HEAD_OF_COMMISSION", adminPrivileges);
        createRoleIfNotFound("DEFAULT_COLLEAGUE", Collections.singletonList(readPrivilege));

        ColleagueRole adminRole =
            colleagueRoleRepository.findByName("HEAD_OF_COMMISSION").orElseThrow();
        colleagueRepository.allowOrGiveRoles("n.valikov@g.nsu.ru",
            Collections.singletonList(adminRole));
        alreadySetup = true;
    }

    @Transactional
    public RolePrivilege createPrivilegeIfNotFound(String name) {
        return rolePrivilegeRepository.findByName(name)
            .orElse(rolePrivilegeRepository.save(new RolePrivilege(name)));
    }

    @Transactional
    public ColleagueRole createRoleIfNotFound(
        String name, List<RolePrivilege> privileges) {
        return colleagueRoleRepository.findByName(name)
            .orElse(colleagueRoleRepository.save(new ColleagueRole(name, privileges)));
    }
}