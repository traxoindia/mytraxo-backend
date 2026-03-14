package com.mytraxo.career.dto;

import lombok.Data;

@Data
public class JobApplicationRequest {

    private String jobId;

    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String emailAddress;
    private String currentAddress;

    private String expectedSalary;
    private String noticePeriod;
    private String availableStartDate;
    private Boolean willingToRelocate;

    private String highestQualification;
    private String degreeName;
    private String universityCollege;
    private String fieldOfStudy;
    private String graduationYear;
    private String percentageGPA;

    private String totalExperience;
    private String currentCompany;
    private String currentJobTitle;
    private String previousCompany;

    private String keySkills;
    private String technicalSkills;
    private String cvFileUrl;
    private String referenceName;
}
 