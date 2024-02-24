package ru.nsu.enrollease.controller;

import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.request.AllowOrGiveRolesRequest;
import ru.nsu.enrollease.dto.response.AllowOrGiveRolesResponse;
import ru.nsu.enrollease.dto.response.ColleagueResponse;
import ru.nsu.enrollease.dto.response.GetAllColleaguesResponse;
import ru.nsu.enrollease.service.ColleagueRoleService;
import ru.nsu.enrollease.service.ColleagueService;

@RestController
@RequestMapping("/colleagues")
@RequiredArgsConstructor
public class ColleagueController {

    private final ColleagueService colleagueService;

    @Operation(summary = "Получить список всех коллег")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список коллег получен", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = GetAllColleaguesResponse.class))
        })})
    @GetMapping
    public GetAllColleaguesResponse findAllColleagues() {
        return new GetAllColleaguesResponse(colleagueService.getAllColleagues().stream()
            .map(i -> new ColleagueResponse(i.getEmail(), i.getRoles())).toList());
    }

//    @PostMapping("/refresh")
//    public AuthenticationResponse refreshToken(HttpServletRequest request,
//        HttpServletResponse response) {
//        return authenticationService.refreshToken(request, response);
//    }
}
