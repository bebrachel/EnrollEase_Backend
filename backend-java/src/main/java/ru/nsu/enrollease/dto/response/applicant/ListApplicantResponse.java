package ru.nsu.enrollease.dto.response.applicant;

import java.util.List;
import lombok.NonNull;

public record ListApplicantResponse(@NonNull List<ApplicantResponse> applicantList) {

}
