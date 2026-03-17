package com.mytraxo.career.dto;

import com.mytraxo.career.enums.ApplicationStage;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InterviewRequest {

    private String applicationId;

    private LocalDate interviewDate;
    private LocalTime interviewTime;

    private ApplicationStage interviewRound;

    private String interviewerName;
    private String interviewLink;
    private String notes;
}