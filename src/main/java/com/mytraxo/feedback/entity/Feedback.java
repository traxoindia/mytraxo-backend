package com.mytraxo.feedback.entity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data

@Document(collection = "feedback")
public class Feedback {

    @Id
    private String id;
    private String applicationId;
    private String candidateName;
    private String email;
    private String phone;
    private String positionApplied;

    private String interviewerId;
    private String interviewerName;

    private Evaluation evaluation;

    private int overallRating;
    private String strengths;
    private String areasOfImprovement;

    private String recommendation; // hire / reject / hold
    private String additionalComments;

    private String status; // submitted / draft

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}