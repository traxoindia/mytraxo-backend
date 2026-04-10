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
    private String token; // Unique UUID for the link
    private String status; // PENDING, BGV_SUBMITTED, ONBOARDING_SUBMITTED, APPROVED, REJECTED

    // 1. Personal & Identity Information
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

    // 2. Educational Verification (List for 10th, 12th, Graduation)
    private List<EducationDetail> educationDetails;

    // 3. Employment Verification
    private List<EmploymentDetail> employmentHistory;
    private String lastDrawnSalary;
    private String criminalRecordDeclaration; // Self-declaration text

    // 4. References
    private List<ReferenceDetail> references;

    // 5. Bank Details (Collected in Onboarding Page)
    private String bankName;
    private String accountNumber;
    private String ifscCode;

    // 6. Documents (Key: Document Type, Value: File Path in /uploads)
    private Map<String, String> documentPaths;
    // You can also add a helper list to track which documents are mandatory
public static final List<String> MANDATORY_DOCS = List.of(
    "10th Marksheet", "12th Marksheet", "Highest Degree", 
    "Aadhar Card", "PAN Card", "Passport Photo"
);
}

// Helper Classes (Inner or separate files)
class EducationDetail { String level; String institute; String passingYear; String percentage; }
class EmploymentDetail { String company; String designation; String duration; String hrContact; }
class ReferenceDetail { String name; String company; String contact; }