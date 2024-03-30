package ru.nsu.enrollease.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.dto.response.colleagues.AllowedColleagueResponse;
import ru.nsu.enrollease.dto.response.colleagues.ColleagueResponse;
import ru.nsu.enrollease.dto.response.colleagues.GetAllColleaguesResponse;
import ru.nsu.enrollease.service.ColleagueService;

@RestController
@RequestMapping("/colleagues")
@RequiredArgsConstructor
public class ColleagueController {

    private final ColleagueService colleagueService;

    @Operation(summary = "Проверить права человека на доступ к нашему сервису")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус пользователя получен", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = AllowedColleagueResponse.class))
            })})
    @GetMapping("allows/{email}")
    public AllowedColleagueResponse isAllowed(@PathVariable String email) {
        return new AllowedColleagueResponse(colleagueService.isAllowed(email));
    }

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
