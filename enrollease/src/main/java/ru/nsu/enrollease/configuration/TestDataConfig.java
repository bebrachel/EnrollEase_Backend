package ru.nsu.enrollease.configuration;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.dto.request.applicant_portfolio.ApplicantPortfolioRequest;
import ru.nsu.enrollease.service.ApplicantPortfolioService;
import ru.nsu.enrollease.service.ColleagueRoleService;
import ru.nsu.enrollease.service.ColleagueService;

@Configuration
@RequiredArgsConstructor
@Log4j2
@Order(2)
@Profile("test_data")
public class TestDataConfig implements
    ApplicationListener<ApplicationReadyEvent> {

    boolean alreadySetup = false;

    private final ApplicantPortfolioService applicantPortfolioService;

//    private final ApplicantService applicantService;
    private final ColleagueRoleService colleagueRoleService;

    private final ColleagueService colleagueService;


    @Transactional
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        log.info("ADDING TEST INFO...");
        if (alreadySetup) {
            return;
        }

        applicantPortfolioService.createNewApplicant(
            new ApplicantPortfolioRequest("123", "2923-11-10T17:00:00.000+00:00",
                "16Nk2xWOys_UwhXxmWeQJw92SolHxKmPc",
                "nocarend@gmail.com", "Niklas", "885"));
        applicantPortfolioService.createNewApplicant(
            new ApplicantPortfolioRequest("234", "2923-11-10T17:00:00.000+00:00",
                "1zUqws_acuMTOmRElW85JAz17sJh6l2Tc",
                "no-nocarend@gmail.com", "Santa", "886"));
        applicantPortfolioService.createNewApplicant(
            new ApplicantPortfolioRequest("345", "2923-11-10T17:00:00.000+00:00",
                "1gbXEyN_PZtXint9oWrkkuyGGaom_FG9M",
                "carend@gmail.com", "Nux", "887"));

        // it happens happens on startup via excel table

//        applicantService.createNewApplicant(new Applicant("123", "good", Map.of("fav", "777")));
//        applicantService.createNewApplicant(new Applicant("234", "bad", Map.of("no-gav", "546")));
//        applicantService.createNewApplicant(new Applicant("345", "ugly", Map.of("gun", "pistol")));

        colleagueService.allowOrGiveRoles("o.pogibelnaya@g.nsu.ru",
            List.of(colleagueRoleService.getRole("HEAD_OF_COMMISSION")));
        colleagueService.allowOrGiveRoles("n.valikov@g.nsu.ru",
            List.of(colleagueRoleService.getRole("DEFAULT_COLLEAGUE")));
        colleagueService.allowOrGiveRoles("m.krikunov@g.nsu.ru",
            List.of(colleagueRoleService.getRole("DEFAULT_COLLEAGUE")));

        alreadySetup = true;
    }
}