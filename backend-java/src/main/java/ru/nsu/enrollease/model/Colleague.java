package ru.nsu.enrollease.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "colleagues")
@AllArgsConstructor
@Data
@Log4j2
@NoArgsConstructor
@ToString
public class Colleague {

    @Id
    @NonNull
    private String email;
    @NonNull
    private List<ColleagueRole> roles;
    private boolean enabled;
    private boolean tokenExpired;
}
