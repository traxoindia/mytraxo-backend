package com.mytraxo.feedback.dto;

import lombok.Data;

@Data
public class FeedbackRequest {

    private String applicationId;

    private String candidateName;
    private String email;
    private String phone;
    private String positionApplied;

    private String interviewerId;
    private String interviewerName;

    private EvaluationDto evaluation;

    private int overallRating;
    private String strengths;
    private String areasOfImprovement;

    private String recommendation;
    private String additionalComments;

    private String status;
}