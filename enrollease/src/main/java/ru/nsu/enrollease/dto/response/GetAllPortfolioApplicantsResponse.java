package ru.nsu.enrollease.dto.response;

import java.util.List;
import ru.nsu.enrollease.model.ApplicantPortfolio;

public record GetAllPortfolioApplicantsResponse(List<ApplicantPortfolio> applicantList) {

}
