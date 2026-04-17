package com.mytraxo.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeRegisterRequest {
    private String employeeId;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String employmentStatus;
    private String designation; // ✅ Add this field
}