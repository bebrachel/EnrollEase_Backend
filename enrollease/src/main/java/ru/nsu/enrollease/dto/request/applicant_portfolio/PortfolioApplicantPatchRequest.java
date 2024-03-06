package ru.nsu.enrollease.dto.request.applicant_portfolio;

import ru.nsu.enrollease.model.ApplicantPortfolio.Status;

public record PortfolioApplicantPatchRequest(String commentary, Status status, int rank) {

}
