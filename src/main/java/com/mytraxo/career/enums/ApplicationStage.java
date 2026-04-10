package com.mytraxo.career.enums;

public enum ApplicationStage {

    APPLIED,
    SCREENING,
    HR_ROUND,
    TECHNICAL_ROUND,
    MANAGERIAL_ROUND,
    SELECTED,          // Trigger: Send BGV Link
    BGV_IN_PROGRESS,   // Candidate is filling Page 1 (Verification)
    ONBOARDING,        // Candidate is filling Page 2 (Details) & HR Review
    HIRED, 
     REJECTED              // Final: Becomes CURRENT employee
}
