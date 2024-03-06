package ru.nsu.enrollease.dto.request.colleagues;

import java.util.List;
import lombok.NonNull;

public record AllowOrGiveRolesRequest(@NonNull String email, @NonNull List<String> roles) {

}
