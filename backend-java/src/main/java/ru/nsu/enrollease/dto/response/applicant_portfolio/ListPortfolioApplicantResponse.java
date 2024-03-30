package ru.nsu.enrollease.dto.response.applicant_portfolio;

import java.util.List;
import ru.nsu.enrollease.dto.response.applicant.ApplicantResponse;

public record ListPortfolioApplicantResponse(List<PortfolioApplicantResponse> applicantList) {

}
