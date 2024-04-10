package ru.nsu.enrollease.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.dto.request.applicant_portfolio.ApplicantPortfolioRequest;
import ru.nsu.enrollease.dto.response.applicant_portfolio.NumberOfApplicantsResponse;
import ru.nsu.enrollease.dto.response.applicant_portfolio.NumberOfCertificatesResponse;
import ru.nsu.enrollease.model.Applicant;
import ru.nsu.enrollease.model.ApplicantPortfolio;
import ru.nsu.enrollease.model.ApplicantPortfolio.Status;
import ru.nsu.enrollease.repository.ApplicantPortfolioRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class ApplicantPortfolioService {

    private final ApplicantPortfolioRepository applicantPortfolioRepository;
    private final ApplicantService applicantService;

    @Transactional
    public ApplicantPortfolio createNewApplicant(
            @NonNull ApplicantPortfolioRequest applicantPortfolioRequest) {
        if (isExists(applicantPortfolioRequest.iian())) {
            throw new IllegalArgumentException("Applicant with such iian is already created.");
        }
        var applicant = applicantPortfolioRepository.save(
                new ApplicantPortfolio(
                        applicantPortfolioRequest.iian(),
                        applicantPortfolioRequest.folderCreationDate(),
                        applicantPortfolioRequest.folderId(),
                        Status.PARTICIPANT,
                        applicantPortfolioRequest.folderCreationDate(),
                        0,
                        "",
                        applicantPortfolioRequest.email(),
                        applicantPortfolioRequest.name(),
                        applicantPortfolioRequest.phoneNumber()));
        log.info(applicant + " Was added");
        return applicant;
    }

    @Transactional
    public List<ApplicantPortfolio> getAllApplicants() {
        return applicantPortfolioRepository.findAll();
    }

    @Transactional
    public NumberOfCertificatesResponse generateApplicants() {
        var applicants = getAllApplicants();
        var finalists = 0;
        var winners = 0;
        for (var i : applicants) {
            String jsonData = "{\"name\":\"%s\", \"iian\":\"%s\", \"role\":\"%s\", \"email\":\"%s\"}".formatted(i.getName(), i.getIian(), i.getStatus(), i.getEmail());
            System.out.println(i.getStatus());
            log.info(jsonData);
            try {
                Files.write(Paths.get("sertificates_pipe.json"), jsonData.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            try {
                // Command to run Python script with JSON data as argument
                String command = "python3 /scripts/googler/certificateMaking.py";

                // Execute the command
                Process p = Runtime.getRuntime().exec(command);

                if (p.onExit().get().exitValue() == 1) {
                    System.out.println(new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
                    System.out.println(new String(p.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));
                    throw new RuntimeException("Certificate making return exit code 1.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (i.getStatus() == Status.WINNER) {
                winners++;
            } else if (i.getStatus() == Status.FINALIST) {
                finalists++;
            }
        }
        return new NumberOfCertificatesResponse(winners, finalists);
    }

    @Transactional
    public NumberOfApplicantsResponse changePermissions(boolean wrote) {
        String status;
        if (wrote) {
            status = "writer";
        } else {
            status = "reader";
        }
        var applicants = getAllApplicants();
        for (var i : applicants) {
            try {
                // Command to run Python script with JSON data as argument
                String command = "python3 /scripts/googler/changeFolderWritingRights.py %s %s %s".formatted(i.getEmail(), i.getFolderId(), status);
                // Execute the command
                Process p = Runtime.getRuntime().exec(command);

                if (p.onExit().get().exitValue() == 1) {
                    System.out.println(new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
                    System.out.println(new String(p.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));
                    throw new RuntimeException("Changing folder rights return exit code 1.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return new NumberOfApplicantsResponse(applicants.size());
    }

    @Transactional
    public NumberOfApplicantsResponse importToOld() {
        var applicants = getAllApplicants();
        for (var i : applicants) {
            Map<String, Object> data = new HashMap<>();
            data.put("ФИО", i.getName());
            data.put("Телефон", i.getPhoneNumber());
            var applicant = new Applicant(i.getIian(), Applicant.Status.PARTICIPANT, data);
            applicantService.createNewApplicant(applicant);
        }
        return new NumberOfApplicantsResponse(applicants.size());
    }

    @Transactional
    public ApplicantPortfolio getApplicant(@NonNull String iian) {
        return applicantPortfolioRepository.findById(iian).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public boolean isExists(@NonNull String iian) {
        return applicantPortfolioRepository.existsByIian(iian);
    }

    @Transactional
    public ApplicantPortfolio setParams(@NonNull String iian, String commentary, Status status, Integer rank) {
        ApplicantPortfolio applicantPortfolio =
                applicantPortfolioRepository.findByIian(iian);
        if (applicantPortfolio != null) {
            if (commentary != null) {
                applicantPortfolio.setCommentaries(commentary);
            }
            if (status != null) {
                applicantPortfolio.setStatus(status);
            }
            if (rank != null) {
                applicantPortfolio.setRank(rank);
            }
            return applicantPortfolioRepository.save(applicantPortfolio);
        }
        throw new IllegalArgumentException("Applicant with this iian does not exist");
    }

    @Transactional
    public ApplicantPortfolio deleteById(String primaryKey) {
        if (!isExists(primaryKey)) {
            throw new IllegalArgumentException("Applicant with this iian does not exist");
        }
        return applicantPortfolioRepository.deleteByIian(primaryKey);
    }

    @Transactional
    public List<ApplicantPortfolio> deleteAll() {
        var response = getAllApplicants();
        applicantPortfolioRepository.deleteAll();
        return response;
    }

}
