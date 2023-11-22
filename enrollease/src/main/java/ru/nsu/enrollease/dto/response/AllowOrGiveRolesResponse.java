package ru.nsu.enrollease.dto.response;


import java.util.List;
import lombok.NonNull;

public record AllowOrGiveRolesResponse(@NonNull String email, @NonNull List<String> roles) {

}
