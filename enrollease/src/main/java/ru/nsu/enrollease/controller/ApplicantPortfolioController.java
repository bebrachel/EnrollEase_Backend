package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.NotImplemented;
import ru.nsu.enrollease.dto.request.ApplicantPortfolioRequest;
import ru.nsu.enrollease.dto.response.GetAllPortfolioApplicantsResponse;
import ru.nsu.enrollease.dto.response.NumberOfCertificatesResponse;
import ru.nsu.enrollease.model.ApplicantPortfolio;
import ru.nsu.enrollease.model.ApplicantPortfolio.Status;
import ru.nsu.enrollease.service.ApplicantPortfolioService;

@RestController
@RequestMapping("/applicants-portfolio")
@RequiredArgsConstructor
public class ApplicantPortfolioController {

    private final ApplicantPortfolioService applicantPortfolioService;

    @Operation(summary = "Добавить абитуриента вручную")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Данные добавленного абитуриента получены", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ApplicantPortfolio.class))
        })})
    @PostMapping
    public ApplicantPortfolio addApplicant(@RequestBody ApplicantPortfolioRequest request) {
        return applicantPortfolioService.createNewApplicant(request);
    }

    @Operation(summary = "Получить список всех абитуриентов в конкурсе портфолио.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список абитуриентов получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = GetAllPortfolioApplicantsResponse.class))
        })})
    @GetMapping
    public GetAllPortfolioApplicantsResponse getAllApplicants() {
        return new GetAllPortfolioApplicantsResponse(applicantPortfolioService.getAllApplicants());
    }

    @Operation(summary = "Проверить СНИЛС абитуриента на его существование.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус абитуриента получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = Boolean.class))
        })})
    @GetMapping("is-exists")
    public boolean isExists(@RequestParam("primaryKey(Снилс)") String primaryKey) {
        return applicantPortfolioService.isExists(primaryKey);
    }

    @Operation(summary = "Изменить параметры абитуриента. Если какой-то не используется, не нужно его указывать вообще.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Параметры изменены", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = String.class))
        })})
    @PatchMapping("changeParams")
    public String changeParams(
        @RequestParam("primaryKey(Снилс)") String primaryKey,
        @RequestBody(required = false) String commentary,
        @RequestBody(required = false) Status status, @RequestBody(required = false) Integer rank) {
        if (!isExists(primaryKey)) {
            throw new IllegalArgumentException("Абитуриент с таким СНИЛС не найден");
        }
        if (rank < 0 || rank > 10) {
            throw new IllegalArgumentException("Ранг должен быть не меньше 0 и не больше 10");
        }
        return applicantPortfolioService.setParams(primaryKey, commentary, status, rank);
    }

    @Operation(summary = "Удалить абитуриента (например, спамера).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Комментарий изменен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ApplicantPortfolio.class))
        })})
    @DeleteMapping
    public ApplicantPortfolio deleteApplicantPortfolio(
        @RequestParam("primaryKey(Снилс)") String primaryKey) {
        if (!isExists(primaryKey)) {
            throw new IllegalArgumentException("Абитуриент с таким СНИЛС не найден");
        }
        return applicantPortfolioService.deleteApplicantPortfolio(primaryKey);
    }

    @Operation(summary = "Сгенерировать сертификаты")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Сгенерировано N сертификатов финалистов и K сертификатов победителей", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = NumberOfCertificatesResponse.class))
        })})
    @GetMapping("generate-sertificates")
    public NumberOfCertificatesResponse generateFinalistCertificates() {
        // Generation Logic and inserting into the same folder.
        // Fuck, we have to use python script here;
        throw new NotImplementedException("Not implemented");
    }

    @Operation(summary = "Импорт абитуриентов в основную базу")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ипортировано N пользователей", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = Integer.class))
        })})
    @GetMapping("import-applicants")
    public Integer importApplicantsToNewDatabase() {
        // Well, just getting all data from portfolio and insert into new one table
        throw new NotImplementedException("Not implemented");
    }
}
