package ru.nsu.enrollease.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.dto.request.ApplicantPortfolioRequest;
import ru.nsu.enrollease.model.ApplicantPortfolio;
import ru.nsu.enrollease.model.ApplicantPortfolio.Status;
import ru.nsu.enrollease.repository.ApplicantPortfolioRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class ApplicantPortfolioService {

    private final ApplicantPortfolioRepository applicantPortfolioRepository;

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
                applicantPortfolioRequest.name()));
        log.info(applicant + " Was added");
        return applicant;
    }

    @Transactional
    public List<ApplicantPortfolio> getAllApplicants() {
        return applicantPortfolioRepository.findAll();
    }

    @Transactional
    public boolean isExists(@NonNull String iian) {
        return applicantPortfolioRepository.existsByIian(iian);
    }

    @Transactional
    public String setParams(@NonNull String iian, String commentary, Status status, Integer rank) {
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
            return applicantPortfolioRepository.save(applicantPortfolio).getCommentaries();
        }
        throw new IllegalArgumentException("Applicant with this iian does not exist");
    }

    @Transactional
    public ApplicantPortfolio deleteApplicantPortfolio(String primaryKey) {
        if (!isExists(primaryKey)) {
            throw new IllegalArgumentException("Applicant with this iian does not exist");
        }
        return applicantPortfolioRepository.deleteByIian(primaryKey);
    }

//    @Transactional
//    public ApplicantPortfolio findApplicantPortfolioByIian(@NonNull String iian){
//        return applicantPortfolioRepository.findByФизическоеЛицоСНИЛС(iian);
//    }

}
