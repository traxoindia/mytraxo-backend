package com.mytraxo.feedback.service;

import org.springframework.stereotype.Service;

import com.mytraxo.feedback.dto.FeedbackRequest;
import com.mytraxo.feedback.dto.RatingDto;
import com.mytraxo.feedback.entity.Feedback;
import com.mytraxo.feedback.entity.Evaluation;
import com.mytraxo.feedback.entity.Rating;
import com.mytraxo.feedback.repo.FeedbackRepository;

import com.mytraxo.career.entity.JobApplication;
import com.mytraxo.career.repo.JobApplicationRepository;
import com.mytraxo.career.enums.ApplicationStage;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository repository;
    private final JobApplicationRepository applicationRepository;

    public FeedbackService(FeedbackRepository repository,
                           JobApplicationRepository applicationRepository) {
        this.repository = repository;
        this.applicationRepository = applicationRepository;
    }

    // ✅ MAIN SAVE METHOD
    public Feedback saveFeedback(FeedbackRequest request) {

        Feedback feedback = new Feedback();

        // Basic fields
        feedback.setApplicationId(request.getApplicationId());
        feedback.setCandidateName(request.getCandidateName());
        feedback.setEmail(request.getEmail());
        feedback.setPhone(request.getPhone());
        feedback.setPositionApplied(request.getPositionApplied());

        feedback.setInterviewerId(request.getInterviewerId());
        feedback.setInterviewerName(request.getInterviewerName());

        feedback.setRecommendation(request.getRecommendation());
        feedback.setStatus(request.getStatus());

        feedback.setOverallRating(request.getOverallRating());
        feedback.setStrengths(request.getStrengths());
        feedback.setAreasOfImprovement(request.getAreasOfImprovement());
        feedback.setAdditionalComments(request.getAdditionalComments());

        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(LocalDateTime.now());

        // ✅ Evaluation Mapping (DTO → Entity)
        if (request.getEvaluation() != null) {

            Evaluation eval = new Evaluation();

            eval.setCommunicationSkills(
                    mapToRating(request.getEvaluation().getCommunicationSkills()));

            eval.setTechnicalKnowledge(
                    mapToRating(request.getEvaluation().getTechnicalKnowledge()));

            eval.setProblemSolving(
                    mapToRating(request.getEvaluation().getProblemSolving()));

            eval.setRelevantExperience(
                    mapToRating(request.getEvaluation().getRelevantExperience()));

            eval.setCulturalFit(
                    mapToRating(request.getEvaluation().getCulturalFit()));

            feedback.setEvaluation(eval);
        }

        // ✅ Fetch Application
        JobApplication app = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // ✅ AUTO STAGE UPDATE
        switch (request.getRecommendation().toLowerCase()) {

            case "hire":
                app.setStage(ApplicationStage.HIRED);
                break;

            case "reject":
                app.setStage(ApplicationStage.REJECTED);
                break;

            case "hold":
                if (app.getStage() == ApplicationStage.TECHNICAL_ROUND) {
                    app.setStage(ApplicationStage.MANAGERIAL_ROUND);
                } else {
                    app.setStage(ApplicationStage.HR_ROUND);
                }
                break;

            default:
                app.setStage(ApplicationStage.SCREENING);
        }

        applicationRepository.save(app);

        // ✅ Save feedback
        return repository.save(feedback);
    }

    // ✅ DTO → ENTITY CONVERTER
    private Rating mapToRating(RatingDto dto) {
        if (dto == null) return null;

        Rating rating = new Rating();
        rating.setRating(dto.getRating());
        rating.setComments(dto.getComments());

        return rating;
    }

    // ✅ GET APIs
    public List<Feedback> getByApplicationId(String applicationId) {
        return repository.findByApplicationId(applicationId);
    }

    public List<Feedback> getAll() {
        return repository.findAll();
    }
}