package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nsu.enrollease.dto.request.applicant_portfolio.ChangePermissionsRequest;
import ru.nsu.enrollease.dto.response.applicant_portfolio.ListPortfolioApplicantResponse;
import ru.nsu.enrollease.dto.response.applicant_portfolio.NumberOfApplicantsResponse;
import ru.nsu.enrollease.dto.response.applicant_portfolio.NumberOfCertificatesResponse;
import ru.nsu.enrollease.dto.response.applicant_portfolio.PortfolioApplicantResponse;
import ru.nsu.enrollease.service.ApplicantPortfolioService;

@RestController
@RequestMapping("/admin/applicants-portfolio")
@RequiredArgsConstructor
public class ApplicantPortfolioAdminController {

    private final ApplicantPortfolioService applicantPortfolioService;

    @Operation(summary = "Сгенерировать сертификаты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сгенерировано N сертификатов финалистов и K сертификатов победителей", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = NumberOfCertificatesResponse.class))
            })})
    @PostMapping("generate-sertificates")
    public NumberOfCertificatesResponse generateFinalistCertificates() {
        // Generation Logic and inserting into the same folder.
        // Fuck, we have to use python script here;
        return applicantPortfolioService.generateApplicants();
    }

    @Operation(summary = "Открыть/закрыть папки с портфолио на запись.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изменен права у N папок", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = NumberOfApplicantsResponse.class))
            })})
    @PostMapping("change-permissions")
    public NumberOfApplicantsResponse changeFolderWritingRights(@RequestBody ChangePermissionsRequest changePermissionsRequest) {
        // Generation Logic and inserting into the same folder.
        // Fuck, we have to use python script here;
        return applicantPortfolioService.changePermissions(changePermissionsRequest.write());
    }

    @Operation(summary = "Импорт абитуриентов в основную базу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ипортировано N пользователей", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = NumberOfApplicantsResponse.class))
            })})
    @PostMapping("import-applicants")
    public NumberOfApplicantsResponse importApplicantsToNewDatabase() {
        // Well, just getting all data from portfolio and insert into new one table
        return applicantPortfolioService.importToOld();
    }

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
