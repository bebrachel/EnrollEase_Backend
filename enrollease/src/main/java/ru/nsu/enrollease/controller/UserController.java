package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.request.CreateOrUpdateUserRequest;
import ru.nsu.enrollease.dto.response.CreateOrUpdateUserResponse;
import ru.nsu.enrollease.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Создать нового пользователя или выдать роли существующему")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Операция успешно выполнена", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = CreateOrUpdateUserResponse.class))
        })})
    @PutMapping
    public CreateOrUpdateUserResponse createOrUpdateUser(
        @RequestBody CreateOrUpdateUserRequest request) {
        var serviceResponse = userService.createOrUpdate(request.email(), request.roles());
        return new CreateOrUpdateUserResponse(serviceResponse.getEmail(),
            serviceResponse.getRoles());
    }

}
