package ru.nsu.enrollease.dto.response;

import java.util.List;
import ru.nsu.enrollease.model.Applicant;
import ru.nsu.enrollease.model.Colleague;

public record GetAllColleaguesResponse(List<Colleague> colleagueList) {

}
