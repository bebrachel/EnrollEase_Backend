package ru.nsu.enrollease.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@AllArgsConstructor
@Data
public class Colleague {

    @Id
    @NonNull
    private String email;
    @NonNull
    private List<String> roles;
}
