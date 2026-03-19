package com.mytraxo.feedback.dto;

import lombok.Data;

@Data
public class EvaluationDto {

    private RatingDto communicationSkills;
    private RatingDto technicalKnowledge;
    private RatingDto problemSolving;
    private RatingDto relevantExperience;
    private RatingDto culturalFit;
}