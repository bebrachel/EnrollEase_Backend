package ru.nsu.enrollease.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "colleagues")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Colleague {

    @Id
    @NonNull
    private String email;
    @NonNull
    private List<ColleagueRole> roles;
    private boolean enabled;
    private boolean tokenExpired;
}
