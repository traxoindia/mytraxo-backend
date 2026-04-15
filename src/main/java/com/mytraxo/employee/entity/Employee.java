package com.mytraxo.employee.entity;
import lombok.Data;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

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
    private String emailAddress;
    private String address;

    // Job Information
    private String department;
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
}