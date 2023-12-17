package ru.nsu.enrollease.model;

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
//    @NonNull for now
    private String status;
    @NonNull
    private Map<String, Object> data;
}
