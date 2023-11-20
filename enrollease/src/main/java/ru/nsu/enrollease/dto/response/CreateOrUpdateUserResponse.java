package ru.nsu.enrollease.dto.response;


import java.util.List;
import lombok.NonNull;

public record CreateOrUpdateUserResponse(@NonNull String email, @NonNull List<String> roles) {

}
