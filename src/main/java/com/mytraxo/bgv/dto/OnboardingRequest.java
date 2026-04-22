package com.mytraxo.bgv.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor; // 👈 Add this
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor  // 👈 Add this line to both files
@AllArgsConstructor

public class OnboardingRequest {
    private String bgvId;
    // Add any other fields the public onboarding form sends
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String emergencyContactName;
    private String emergencyContactNumber;
}