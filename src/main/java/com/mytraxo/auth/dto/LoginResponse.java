package com.mytraxo.auth.dto;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String status; // SUCCESS, INVALID_EMAIL, INVALID_PASSWORD, ACCOUNT_LOCKED
    private String message;
    private String accessToken; // The JWT Access Token
    private String refreshToken;
    private EmployeeProfileDTO profile;
}