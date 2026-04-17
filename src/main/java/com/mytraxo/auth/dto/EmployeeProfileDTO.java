package com.mytraxo.auth.dto;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfileDTO {
    private String employeeId;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String designation;
    private String profilePicture; 
}