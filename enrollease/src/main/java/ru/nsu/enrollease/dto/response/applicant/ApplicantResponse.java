package ru.nsu.enrollease.dto.response.applicant;

import lombok.NonNull;
import ru.nsu.enrollease.model.Applicant;

public record ApplicantResponse(@NonNull Applicant applicant) {

}
