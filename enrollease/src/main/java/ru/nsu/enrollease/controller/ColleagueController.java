package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.request.AllowOrGiveRolesRequest;
import ru.nsu.enrollease.dto.response.AllowOrGiveRolesResponse;
import ru.nsu.enrollease.dto.response.GetAllApplicantsResponse;
import ru.nsu.enrollease.dto.response.GetAllColleaguesResponse;
import ru.nsu.enrollease.service.ColleagueService;

@RestController
@RequestMapping("/colleagues")

@RequiredArgsConstructor
public class ColleagueController {

    private final ColleagueService colleagueService;

    @Operation(summary = "Разрешить человеку доступ к нашему сервису или выдать роли уже существующему")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Операция успешно выполнена", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = AllowOrGiveRolesResponse.class))
        })})
    @PutMapping
    public AllowOrGiveRolesResponse allowOrGiveRoles(
        @RequestBody AllowOrGiveRolesRequest request) {
        var serviceResponse = colleagueService.allowOrGiveRoles(request.email(), request.roles());
        return new AllowOrGiveRolesResponse(serviceResponse.getEmail(),
            serviceResponse.getRoles());
    }


    /**
     * Можно валидировать через код запроса, а можно через TRUE/FALSE
     */
    @Operation(summary = "Проверить права человека на доступ к нашему сервису")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус пользователя получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = Boolean.class))
        })})
    @GetMapping("is_exists")
    public boolean isAllowed(@RequestParam("token") String token) {
        return colleagueService.isAllowed(token);
    }

    @Operation(summary = "Получить список всех коллег")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список коллег получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = GetAllColleaguesResponse.class))
        })})
    @GetMapping
    public GetAllColleaguesResponse findAllColleagues() {
        return new GetAllColleaguesResponse(colleagueService.getAllColleagues());
    }

}
