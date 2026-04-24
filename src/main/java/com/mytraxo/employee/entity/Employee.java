package com.mytraxo.employee.entity;
import lombok.Data;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;
//import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    private String id;
    private String role;
    // Basic Information
    private String employeeId;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    //@Indexed(unique = true)
    private String emailAddress;
    private String address;

    // Job Information
    private String department;
     @JsonProperty("designation") 
    private String designation;
    private String reportingManager;
    private String employeeType;
    private String dateOfJoining;
    private String workLocation;
    private EmployeeStatus employmentStatus;

    // Payroll Information
    private String salary;
    private String bankAccountNumber;
    private String bankName;
    private String ifscCode;
    private String panNumber;

    // Identity Documents
    private String aadhaarNumber;
    private String panCard;
    private String passport;

    // Other Details
    private String emergencyContactName;
    private String emergencyContactNumber;
    private String educationQualification;
    private String previousWorkExperience;

    // Document Upload (file names)
    private String resume;
    private String aadhaarCard;
    private String panCardDoc;
    private String offerLetter;
    private String educationalCertificates;
  
    // Leave employee details
    private String leavingDate;
    private String leavingReason;
    private Double monthlySalary; // e.g., 50000.0
   // private String status;        // ACTIVE, INACTIVE, RESIGNED
    private LocalDate exitDate;   // To be filled during settlement
    // ==========================================
    // NEW FIELDS FOR MOBILE (ADDITIVE - WON'T BREAK WEB)
    // ==========================================

    
   // --- CLEAN NAMES FOR MOBILE (LISTS) ---
    private List<String> designations;      // Plural: List for Mobile
    private List<WorkExperienceDetail> workHistory;
    private List<EducationDetail> educationHistory;

    
    private String bloodGroup;
    private String maritalStatus;

    
    private String employmentStage; 
    private boolean selfServiceOn;
    private String probationEndDate;
    private String dateOfConfirmation;
    private String officeAddressDetail;

    // --- HELPER CLASSES (Nested inside Employee class) ---

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkExperienceDetail {
        private String role;
        private String duration;
        private String dateRange;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EducationDetail {
        private String level;
        private String year;
        private String degree;
        private String institution;
    }
}
