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
import ru.nsu.enrollease.dto.response.applicant.ApplicantResponse;
import ru.nsu.enrollease.dto.response.applicant.ListApplicantResponse;
import ru.nsu.enrollease.service.ApplicantService;

@RestController
@RequestMapping("/admin/applicants")
@RequiredArgsConstructor
public class ApplicantAdminController {

    private final ApplicantService applicantService;

    @Operation(summary = "Удалить все данные")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список удаленных абитуриентов получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ListApplicantResponse.class))
        })})
    @DeleteMapping
    public ListApplicantResponse deleteAllApplicants() {
        return new ListApplicantResponse(applicantService.deleteAll().stream().map(
            ApplicantResponse::new).toList());
    }
}
