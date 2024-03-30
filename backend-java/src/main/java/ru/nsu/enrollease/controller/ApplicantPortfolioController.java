package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.request.applicant_portfolio.ApplicantPortfolioRequest;
import ru.nsu.enrollease.dto.request.applicant_portfolio.PortfolioApplicantPatchRequest;
import ru.nsu.enrollease.dto.response.applicant_portfolio.ListPortfolioApplicantResponse;
import ru.nsu.enrollease.dto.response.applicant_portfolio.PortfolioApplicantResponse;
import ru.nsu.enrollease.model.ApplicantPortfolio;
import ru.nsu.enrollease.service.ApplicantPortfolioService;

@RestController
@RequestMapping("/applicants-portfolio")
@RequiredArgsConstructor
public class ApplicantPortfolioController {

    private final ApplicantPortfolioService applicantPortfolioService;

    @Operation(summary = "Получить список всех абитуриентов в конкурсе портфолио.\n Если указан iian - получаем конкретного.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список абитуриентов получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ListPortfolioApplicantResponse.class))
        })})
    @GetMapping
    public ListPortfolioApplicantResponse getApplicants(
        @RequestParam(required = false) String iian) {
        if (iian != null) {
            return new ListPortfolioApplicantResponse(
                Stream.of(applicantPortfolioService.getApplicant(iian))
                    .map(PortfolioApplicantResponse::new)
                    .toList());
        }
        return new ListPortfolioApplicantResponse(
            applicantPortfolioService.getAllApplicants().stream()
                .map(PortfolioApplicantResponse::new).toList());
    }

    @Operation(summary = "Проверить СНИЛС абитуриента на его существование.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус абитуриента получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = Boolean.class))
        })})
    @GetMapping("exists/{iian}")
    public boolean exists(@PathVariable String iian) {
        return applicantPortfolioService.isExists(iian);
    }

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

    @Operation(summary = "Изменить параметры абитуриента. Если какой-то не используется, не нужно его указывать вообще.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Параметры изменены", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = String.class))
        })})
    @PatchMapping("/{iian}")
    public PortfolioApplicantResponse changeParams(
        @PathVariable String iian,
        @RequestBody PortfolioApplicantPatchRequest applicantPortfolioRequest) {
        if (!exists(iian)) {
            throw new IllegalArgumentException("Абитуриент с таким СНИЛС не найден");
        }
        if (applicantPortfolioRequest.rank() < 0 || applicantPortfolioRequest.rank() > 10) {
            throw new IllegalArgumentException("Ранг должен быть не меньше 0 и не больше 10");
        }
        return new PortfolioApplicantResponse(applicantPortfolioService.setParams(iian,
            applicantPortfolioRequest.commentary(), applicantPortfolioRequest.status(),
            applicantPortfolioRequest.rank()));
    }

    @Operation(summary = "Удалить выбранного абитуриента")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус абитуриента получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = PortfolioApplicantResponse.class))
        })})
    @DeleteMapping("/{iian}")
    public PortfolioApplicantResponse delete(@PathVariable String iian) {
        return new PortfolioApplicantResponse(applicantPortfolioService.deleteById(iian));
    }
}
