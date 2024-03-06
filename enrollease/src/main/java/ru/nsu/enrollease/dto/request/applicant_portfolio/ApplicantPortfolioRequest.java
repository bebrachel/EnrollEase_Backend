package ru.nsu.enrollease.dto.request.applicant_portfolio;

import lombok.NonNull;

public record ApplicantPortfolioRequest(@NonNull String iian,
                                        @NonNull String folderCreationDate, @NonNull String folderId,
                                        @NonNull String email, @NonNull String name, @NonNull String phoneNumber) {

}
