package ru.nsu.enrollease.dto.response.applicant_portfolio;

import lombok.NonNull;
import ru.nsu.enrollease.model.Applicant;
import ru.nsu.enrollease.model.ApplicantPortfolio;

public record PortfolioApplicantResponse(@NonNull ApplicantPortfolio applicant) {

}
