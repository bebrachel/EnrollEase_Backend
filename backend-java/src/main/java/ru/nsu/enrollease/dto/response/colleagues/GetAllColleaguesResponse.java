package ru.nsu.enrollease.dto.response.colleagues;

import java.util.List;
import ru.nsu.enrollease.dto.response.colleagues.ColleagueResponse;

public record GetAllColleaguesResponse(List<ColleagueResponse> colleagueList) {

}
