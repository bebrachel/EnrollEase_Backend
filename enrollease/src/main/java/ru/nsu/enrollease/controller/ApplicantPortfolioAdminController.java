package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.response.applicant_portfolio.ListPortfolioApplicantResponse;
import ru.nsu.enrollease.dto.response.applicant_portfolio.PortfolioApplicantResponse;
import ru.nsu.enrollease.service.ApplicantPortfolioService;

@RestController
@RequestMapping("/admin/applicants-portfolio")
@RequiredArgsConstructor
public class ApplicantPortfolioAdminController {

    private final ApplicantPortfolioService applicantPortfolioService;

//    @Operation(summary = "Сгенерировать сертификаты")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Сгенерировано N сертификатов финалистов и K сертификатов победителей", content = {
//            @Content(mediaType = "application/json", schema =
//            @Schema(implementation = NumberOfCertificatesResponse.class))
//        })})
//    @PostMapping("generate-sertificates")
//    public NumberOfCertificatesResponse generateFinalistCertificates() {
//        // Generation Logic and inserting into the same folder.
//        // Fuck, we have to use python script here;
//        throw new NotImplementedException("Not implemented");
//    }
    //    @Operation(summary = "Импорт абитуриентов в основную базу")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Ипортировано N пользователей", content = {
//            @Content(mediaType = "application/json", schema =
//            @Schema(implementation = Integer.class))
//        })})
//    @PostMapping("import-applicants")
//    public Integer importApplicantsToNewDatabase() {
//        // Well, just getting all data from portfolio and insert into new one table
//        throw new NotImplementedException("Not implemented");
//    }

    @Operation(summary = "Удалить все данные")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список удаленных абитуриентов получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ListPortfolioApplicantResponse.class))
        })})
    @DeleteMapping
    public ListPortfolioApplicantResponse deleteAllApplicantsPortfolio() {
        return new ListPortfolioApplicantResponse(
            applicantPortfolioService.deleteAll().stream().map(
                PortfolioApplicantResponse::new).toList());
    }
}
