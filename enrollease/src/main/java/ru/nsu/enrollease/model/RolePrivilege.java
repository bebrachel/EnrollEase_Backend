package ru.nsu.enrollease.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "privileges")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolePrivilege {

    @Id
    @NonNull
    private String name;

}