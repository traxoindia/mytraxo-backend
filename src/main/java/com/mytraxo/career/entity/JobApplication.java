package com.mytraxo.career.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "job_applications")
public class JobApplication {

    @Id
    private String id;

    private String jobId;

    // Personal
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String emailAddress;
    private String currentAddress;

    // Professional
    private String expectedSalary;
    private String noticePeriod;
    private String availableStartDate;
    private Boolean willingToRelocate;

    // Education
    private String highestQualification;
    private String degreeName;
    private String universityCollege;
    private String fieldOfStudy;
    private String graduationYear;
    private String percentageGPA;

    // Experience
    private String totalExperience;
    private String currentCompany;
    private String currentJobTitle;
    private String previousCompany;

    // Skills
    private String keySkills;
    private String technicalSkills;

    // Resume
    private String cvFileUrl;

    // Reference
    private String referenceName;

    private String status;
    private Instant appliedAt;
}