package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.request.colleagues.AllowOrGiveRolesRequest;
import ru.nsu.enrollease.dto.response.colleagues.AllowOrGiveRolesResponse;
import ru.nsu.enrollease.dto.response.colleagues.ColleagueResponse;
import ru.nsu.enrollease.dto.response.colleagues.AllowedColleagueResponse;
import ru.nsu.enrollease.service.ColleagueRoleService;
import ru.nsu.enrollease.service.ColleagueService;

@RestController
@RequestMapping("/admin/colleagues")
@RequiredArgsConstructor
public class ColleagueAdminController {

    private final ColleagueService colleagueService;
    private final ColleagueRoleService colleagueRoleService;

//    @Operation(summary = "Разрешить или запретить уже существующему пользователю использовать сервис")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Статус изменён", content = {
//            @Content(mediaType = "application/json", schema =
//            @Schema(implementation = ColleagueActiveStatusResponse.class))
//        })})
//    @PostMapping("/admin/colleagues/set_active_status")
//    public ColleagueActiveStatusResponse setActiveStatus(@RequestParam String email) {
//        return new ColleagueActiveStatusResponse(colleagueService.isAllowed(email));
//    }

    @Operation(summary = "Разрешить человеку доступ к нашему сервису и установить указанные роли")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Операция успешно выполнена", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = AllowOrGiveRolesResponse.class))
        })})
    @PutMapping
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
    @DeleteMapping("/{email}")
    public ColleagueResponse deleteColleague(@PathVariable String email) {
        var response = colleagueService.deleteColleagueByEmail(email);
        return new ColleagueResponse(response.getEmail(), response.getRoles());
    }

    // deleteAll
}
