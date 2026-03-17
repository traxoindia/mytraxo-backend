package com.mytraxo.career.entity;

import com.mytraxo.career.enums.ApplicationStage;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "interview_schedule")
public class InterviewSchedule {

    @Id
    private String id;

    private String applicationId;   // link with JobApplication

    private LocalDate interviewDate;
    private LocalTime interviewTime;

    private ApplicationStage interviewRound; // HR, TECHNICAL, MANAGERIAL

    private String interviewerName;
    private String interviewLink;
    private String notes;

    private String status; // SCHEDULED / COMPLETED / CANCELLED
}