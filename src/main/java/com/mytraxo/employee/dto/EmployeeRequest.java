package com.mytraxo.employee.dto;

import lombok.Data;

@Data
public class EmployeeRequest {

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
    private String employmentStatus;

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
}