package ru.nsu.enrollease.dto.response.colleagues;


import java.util.List;
import lombok.NonNull;
import ru.nsu.enrollease.model.ColleagueRole;

public record AllowOrGiveRolesResponse(@NonNull String email, @NonNull List<ColleagueRole> roles) {

}
