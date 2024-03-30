package ru.nsu.enrollease.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applicants")
@AllArgsConstructor
@Data
@ToString
public class Applicant {

    @Id
    @NonNull
    private String iian;
    //    @NonNull for now
    private String status;
    @NonNull
    private Map<String, Object> data;

    public enum Status {
        PARTICIPANT,
        MAN_OF_ACTION,
        MAN_OF_FUN,
        UNDEFINED;
    }
}
