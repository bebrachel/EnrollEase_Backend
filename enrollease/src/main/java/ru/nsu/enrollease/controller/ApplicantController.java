package ru.nsu.enrollease.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.enrollease.service.ColleagueService;

@RestController
@RequestMapping("/applicants")
@RequiredArgsConstructor
public class ApplicantController {
    private final ColleagueService colleagueService;

//    public void updateDataOrCreateOne()
}
