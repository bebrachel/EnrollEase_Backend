package ru.nsu.enrollease.dto.request;

import java.util.Date;
import lombok.NonNull;

public record ApplicantPortfolioRequest(@NonNull String iian,
                                        @NonNull Date folderCreationDate, @NonNull String folderId,
                                        @NonNull String email, @NonNull String name) {

}
