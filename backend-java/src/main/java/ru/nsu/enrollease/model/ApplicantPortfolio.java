package ru.nsu.enrollease.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applicants_portfolio")
@AllArgsConstructor
@Data
@ToString
@Log4j2
public class ApplicantPortfolio {

    @Id
    @NonNull
    private String iian;
    //    @NonNull for now
    @NonNull
    private String folderCreationDate;
    @NonNull
    private String folderId;
    @NonNull
    private Status status;
    @NonNull
    private String folderUpdatedAt; //not a date because google gives string
    @NonNull
    private Integer rank;
    @NonNull
    private String commentaries;
    @NonNull
    private String email;
    @NonNull
    private String name;

    @NonNull
    private String phoneNumber;
    // for adding test data
//    public ApplicantPortfolio(String iian, Date date, String folderId, String email, String name) {
//        this.iian = iian;
//        if (rank < 0 || rank > 10) {
//            log.error("TRY TO SET UP WRONG PORTFOLIO RANK");
//            throw new AssertionError("Rank must be inside [0, 10]");
//        }
//        this.folderCreationDate = date;
//        this.folderId = folderId;
//        this.status = Status.PARTICIPANT;
//        this.folderUpdatedAt = date;
//        this.rank = 0;
//        this.commentaries = "";
//        this.email = email;
//        this.name = name;
//    }

    public enum Status {
        PARTICIPANT,
        FINALIST,
        WINNER;
    }
}
