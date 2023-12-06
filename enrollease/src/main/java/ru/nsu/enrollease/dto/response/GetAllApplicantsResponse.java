package ru.nsu.enrollease.dto.response;

import java.util.List;
import ru.nsu.enrollease.model.Applicant;

public record GetAllApplicantsResponse(List<Applicant> applicantList) {

}
