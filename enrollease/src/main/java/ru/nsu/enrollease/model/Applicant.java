package ru.nsu.enrollease.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applicants")
@AllArgsConstructor
@Data
public class Applicant {
    @Id
    @NonNull
    private String ФизическоеЛицоСНИЛС;
    @NonNull
    private Map<String, Object> data;
}
