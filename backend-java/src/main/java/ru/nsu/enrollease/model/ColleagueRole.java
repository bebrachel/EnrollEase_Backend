package ru.nsu.enrollease.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ColleagueRole {

    @Id
    @NonNull
    private String name;
    @NonNull
    private List<RolePrivilege> privileges;
}