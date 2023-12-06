package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.response.GetAllApplicantsResponse;
import ru.nsu.enrollease.service.ApplicantService;
import ru.nsu.enrollease.service.ColleagueService;

@RestController
@RequestMapping("/applicants")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;

    //    public void updateDataOrCreateOne()
    private final ColleagueService colleagueService;

    @Operation(summary = "Получить список всех абитуриентов.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список абитуриентов получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = GetAllApplicantsResponse.class))
        })})
    @GetMapping
    public GetAllApplicantsResponse getAllApplicants() {
        System.out.println(applicantService.getAllApplicants());
        return new GetAllApplicantsResponse(applicantService.getAllApplicants());
    }

    @Operation(summary = "Проверить СНИЛС абитуриента на его существование.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус абитуриента получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = Boolean.class))
        })})
    @GetMapping("is_exists")
    public boolean isExists(@RequestParam("primaryKey(Снилс)") String primaryKey) {
        return colleagueService.isAllowed(primaryKey);
    }
}
