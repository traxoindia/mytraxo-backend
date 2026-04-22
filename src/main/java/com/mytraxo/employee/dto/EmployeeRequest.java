package com.mytraxo.employee.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import com.mytraxo.employee.entity.Employee;

import java.util.List;

import lombok.AllArgsConstructor;


@NoArgsConstructor  // Add this
@AllArgsConstructor // Add this
@Builder
@Data
public class EmployeeRequest {

    // Basic Information
    private String role;
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
    private Double monthlySalary;
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
    private List<String> designations; 
    private List<Employee.WorkExperienceDetail> workHistory;
    private List<Employee.EducationDetail> educationHistory;
    private String bloodGroup;
    private String maritalStatus;
    private String employmentStage; 
    private boolean selfServiceOn;
    private String probationEndDate;
    private String dateOfConfirmation;
    private String officeAddressDetail;
}