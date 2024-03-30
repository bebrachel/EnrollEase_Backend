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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.response.applicant.ApplicantResponse;
import ru.nsu.enrollease.dto.response.applicant.ApplicantsExistResponse;
import ru.nsu.enrollease.dto.response.applicant.ListApplicantResponse;
import ru.nsu.enrollease.service.ApplicantService;

@RestController
@RequestMapping("/applicants")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;

    @Operation(summary = "Получить список всех абитуриентов. \n При передаче iian получить данные одного абитуриента.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список абитуриентов получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ListApplicantResponse.class))
        })})
    @GetMapping
    public ListApplicantResponse getApplicants(@RequestParam(required = false) String iian) {
        if (iian != null) {
            return new ListApplicantResponse(
                Stream.of(applicantService.getApplicant(iian)).map(ApplicantResponse::new)
                    .toList());
        }
        return new ListApplicantResponse(applicantService.getAllApplicants().stream().map(
            ApplicantResponse::new).toList());
    }

    @Operation(summary = "Проверить СНИЛС абитуриента на его существование.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус абитуриента получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = Boolean.class))
        })})
    @GetMapping("exists/{iian}")
    public ApplicantsExistResponse exists(@PathVariable String iian) {
        return new ApplicantsExistResponse(applicantService.exists(iian));
    }

    @Operation(summary = "Удалить выбранного абитуриента")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус абитуриента получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ApplicantResponse.class))
        })})
    @DeleteMapping("/{iian}")
    public ApplicantResponse delete(@PathVariable String iian) {
        return new ApplicantResponse(applicantService.deleteById(iian));
    }

//    @Operation(summary = "Создать нового абитуриента")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Статус абитуриента получен", content = {
//            @Content(mediaType = "application/json", schema =
//            @Schema(implementation = Boolean.class))
//        })})
//    @GetMapping("exists/{iian}")
//    public ApplicantsExistResponse exists(@PathVariable String iian) {
//        return new ApplicantsExistResponse(applicantService.exists(iian));
//    }

//    @Operation(summary = "Изменить данные абитуриента")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Статус абитуриента получен", content = {
//            @Content(mediaType = "application/json", schema =
//            @Schema(implementation = Boolean.class))
//        })})
//    @PatchMapping("{iian}")
//    public ApplicantsExistResponse exists(@PathVariable String iian) {
//        return new ApplicantsExistResponse(applicantService.exists(iian));
//    }
}
