package ru.nsu.enrollease.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.model.ColleagueRole;
import ru.nsu.enrollease.model.RolePrivilege;
import ru.nsu.enrollease.repository.ColleagueRoleRepository;
import ru.nsu.enrollease.repository.RolePrivilegeRepository;
import ru.nsu.enrollease.service.ColleagueService;

@Configuration
@RequiredArgsConstructor
@Log4j2
@Order(1)
@Profile({"prod", "test_data"})
public class SetupDataLoaderConfig implements
    ApplicationListener<ApplicationReadyEvent> {

    @Value("${ADMIN_EMAIL:gudvin0203@gmail.com}")
    private String adminEmail;
    boolean alreadySetup = false;

    private final ColleagueService colleagueService;

    private final ColleagueRoleRepository colleagueRoleRepository;

    private final RolePrivilegeRepository rolePrivilegeRepository;

    @Transactional
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        log.info("Setting up database data...");
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
        createAdminIfNotFound();
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

    @Transactional
    public void createAdminIfNotFound() {
        if (colleagueService.allowOrGiveRoles(adminEmail,
                Collections.singletonList(
                    colleagueRoleRepository.findByName("HEAD_OF_COMMISSION").orElseThrow())).getEmail()
            .equals(adminEmail)) {
            log.info(String.format("Admin %s has been created.", adminEmail));
        }
    }
}