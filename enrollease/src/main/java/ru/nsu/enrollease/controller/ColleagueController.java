package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.request.AllowOrGiveRolesRequest;
import ru.nsu.enrollease.dto.response.AllowOrGiveRolesResponse;
import ru.nsu.enrollease.dto.response.ColleagueResponse;
import ru.nsu.enrollease.dto.response.GetAllColleaguesResponse;
import ru.nsu.enrollease.model.Colleague;
import ru.nsu.enrollease.service.ColleagueRoleService;
import ru.nsu.enrollease.service.ColleagueService;

@RestController
@RequiredArgsConstructor
public class ColleagueController {

    private final ColleagueService colleagueService;
    private final ColleagueRoleService colleagueRoleService;

    @Operation(summary = "Разрешить человеку доступ к нашему сервису и установить указанные роли")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Операция успешно выполнена", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = AllowOrGiveRolesResponse.class))
        })})
    @PutMapping("/admin/colleagues")
    public AllowOrGiveRolesResponse allowOrGiveRoles(
        @RequestBody AllowOrGiveRolesRequest request) {
        var serviceResponse =
            colleagueService.allowOrGiveRoles(request.email(),
                request.roles().stream().map(colleagueRoleService::getRole).toList());
        return new AllowOrGiveRolesResponse(serviceResponse.getEmail(),
            serviceResponse.getRoles());
    }

    @Operation(summary = "Удалить человека из списка коллег")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Удаление прошло успешно")
    })
    @DeleteMapping("/admin/colleagues")
    public ResponseEntity<ColleagueResponse> deleteColleague(@RequestParam String email) {
        colleagueService.deleteColleagueByEmail(email);
        return ResponseEntity.ok().build();
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
    @GetMapping("/admin/colleagues/is_allowed")
    public boolean isAllowed(@RequestParam("email") String email) {
        return colleagueService.isAllowed(email);
    }

    @Operation(summary = "Разрешить или запретить уже существующему пользователю использовать сервис")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус изменён", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = Boolean.class))
        })})
    @PostMapping("/admin/colleagues/set_active_status")
    public boolean setActiveStatus(@RequestParam("email") String email) {
        return colleagueService.isAllowed(email);
    }

    @Operation(summary = "Получить список всех коллег")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список коллег получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = GetAllColleaguesResponse.class))
        })})
    @GetMapping("/colleagues")
    public GetAllColleaguesResponse findAllColleagues() {
        return new GetAllColleaguesResponse(colleagueService.getAllColleagues());
    }

}
