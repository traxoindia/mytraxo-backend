package com.mytraxo.bgv.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "bgv_submissions")
public class BGVSubmission {
    @Id
    private String id;
    private String applicationId;
    private String emailAddress;
    private String token; 
    private String status; 

    private String fullName;
    private String dob;
    private String contactNumber;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private String currentAddress;
    private String permanentAddress;
    private String aadharNumber;
    private String panNumber;
    private String passportNumber;

    // Lists for helper classes
    private List<EducationDetail> educationDetails;
    private List<EmploymentDetail> employmentHistory;
    private List<ReferenceDetail> references;

    private String lastDrawnSalary;
    private String criminalRecordDeclaration; 

    private String bankName;
    private String accountNumber;
    private String ifscCode;

    private Map<String, String> documentPaths;

    public static final List<String> MANDATORY_DOCS = List.of(
        "10th Marksheet", "12th Marksheet", "Highest Degree", 
        "Aadhar Card", "PAN Card", "Passport Photo"
    );

    // ✅ FIXED HELPER CLASSES: Added static and Lombok annotations
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationDetail { 
        private String level; 
        private String institute; 
        private String passingYear; 
        private String percentage; 
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmploymentDetail { 
        private String company; 
        private String designation; 
        private String duration; 
        private String hrContact; 
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferenceDetail { 
        private String name; 
        private String company; 
        private String contact; 
    }
}