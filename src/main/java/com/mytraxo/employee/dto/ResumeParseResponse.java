package com.mytraxo.employee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeParseResponse {
    private String fullName;
    private String phoneNumber;
    private String emailAddress;
    private String educationQualification;
    private String previousWorkExperience;
    private String resume;
}