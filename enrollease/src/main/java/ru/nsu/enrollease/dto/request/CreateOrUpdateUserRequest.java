package ru.nsu.enrollease.dto.request;

import java.util.List;
import lombok.NonNull;

public record CreateOrUpdateUserRequest(@NonNull String email, @NonNull List<String> roles) {

}
