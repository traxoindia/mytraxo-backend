package com.mytraxo.career.entity;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.mytraxo.career.enums.ApplicationStage;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
     // ADD THIS LINE BELOW
    private String department; 
    // Resume
    private String cvFileUrl;

    // Reference
    private String referenceName;

    // recruitment stage
    private ApplicationStage stage;

     // status
   private String status;
    private Instant appliedAt;
}