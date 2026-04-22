package com.mytraxo.bgv.service;

import com.mytraxo.bgv.entity.BGVSubmission;
import com.mytraxo.bgv.repo.BGVRepository;
import com.mytraxo.career.enums.ApplicationStage;
import com.mytraxo.career.entity.JobApplication;
import com.mytraxo.career.repo.JobApplicationRepository;
import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.entity.EmployeeStatus;
import com.mytraxo.employee.repo.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map; // <--- ADD THIS IMPORT TO FIX THE 'Map' ERROR
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class BGVService {

    private final BGVRepository bgvRepo;
    private final JobApplicationRepository careerRepo;
    private final EmployeeRepository employeeRepo; // Must be "final"
    // 1. INITIATE: Career (SELECTED) -> BGV
    public void initiateBGVProcess(String appId) {
        JobApplication app = careerRepo.findById(appId).orElseThrow();
        
        BGVSubmission bgv = new BGVSubmission();
        bgv.setApplicationId(appId);
        bgv.setEmailAddress(app.getEmailAddress()); 
        bgv.setToken(UUID.randomUUID().toString());
        bgv.setStatus("PENDING_BGV"); 
        bgvRepo.save(bgv);

        app.setStage(ApplicationStage.BGV_IN_PROGRESS);
        careerRepo.save(app);
    }
// Inside BGVService class
public BGVSubmission getDetailsByToken(String token) {
    return bgvRepo.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Error: Invalid or expired BGV link."));
}
    // This handles the JSON you asked about (Personal, Edu, Employment)
    public void submitVerification(String token, BGVSubmission submissionData) {
        // Use .orElseThrow() to handle the Optional
        BGVSubmission bgv = bgvRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token: " + token));

        bgv.setFullName(submissionData.getFullName());
        bgv.setDob(submissionData.getDob());
        bgv.setContactNumber(submissionData.getContactNumber());
        bgv.setCurrentAddress(submissionData.getCurrentAddress());
        bgv.setAadharNumber(submissionData.getAadharNumber());
        bgv.setPanNumber(submissionData.getPanNumber());
        bgv.setPassportNumber(submissionData.getPassportNumber());
        
        // Lists are mapped automatically
        bgv.setEducationDetails(submissionData.getEducationDetails());
        bgv.setEmploymentHistory(submissionData.getEmploymentHistory());
        
        bgv.setStatus("BGV_SUBMITTED");
        bgvRepo.save(bgv);
    }
    // 1. HR Approves BGV (Creates the Employee record in ONBOARDING status)

    public String approveBGVStage(String bgvId) {
        // Find BGV Record
        BGVSubmission bgv = bgvRepo.findById(bgvId)
                .orElseThrow(() -> new RuntimeException("BGV Not Found"));
        bgv.setStatus("BGV_APPROVED"); 
        bgvRepo.save(bgv);

        // Update Career Application
        JobApplication app = careerRepo.findById(bgv.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application Not Found"));
        app.setStage(ApplicationStage.ONBOARDING);
        careerRepo.save(app);

        // Create the Employee record (using the logic you provided)
        Employee emp = Employee.builder()
            .employeeId("EMP-" + UUID.randomUUID().toString().substring(0,5).toUpperCase())
            .fullName(bgv.getFullName())
            .emailAddress(bgv.getEmailAddress())
            .dateOfBirth(bgv.getDob())
            .phoneNumber(bgv.getContactNumber())
            .address(bgv.getCurrentAddress())
            .aadhaarNumber(bgv.getAadharNumber())
            .panNumber(bgv.getPanNumber())
            .passport(bgv.getPassportNumber())
            .employmentStatus(EmployeeStatus.ONBOARDING) 
            .build();
            
        employeeRepo.save(emp);

        return bgvId; // Returning bgvId as you requested
    }
    // STEP 4: Candidate Submits Onboarding (Bank/Emergency) -> Status updated
  public void submitOnboarding(String token, BGVSubmission onboardingData) {
        // Use .orElseThrow() to handle the Optional
        BGVSubmission bgv = bgvRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token: " + token));

        bgv.setBankName(onboardingData.getBankName());
        bgv.setAccountNumber(onboardingData.getAccountNumber());
        bgv.setIfscCode(onboardingData.getIfscCode());
        bgv.setEmergencyContactName(onboardingData.getEmergencyContactName());
        bgv.setEmergencyContactNumber(onboardingData.getEmergencyContactNumber());

        bgv.setStatus("ONBOARDING_SUBMITTED"); // Candidate is now finished with all forms
        bgvRepo.save(bgv);
    }


    // 3. REJECT
    public void rejectCandidateProcess(String bgvId) {
        BGVSubmission bgv = bgvRepo.findById(bgvId).orElseThrow();
        bgv.setStatus("REJECTED");
        bgvRepo.save(bgv);

        JobApplication app = careerRepo.findById(bgv.getApplicationId()).orElseThrow();
        app.setStage(ApplicationStage.REJECTED);
        careerRepo.save(app);
    }
    public Map<String, String> bulkFinalizeEmployees(List<String> empIds) {
    Map<String, String> results = new java.util.HashMap<>();
    
    for (String empId : empIds) {
        try {
            finalizeEmployee(empId);
            results.put(empId, "SUCCESS");
        } catch (Exception e) {
            results.put(empId, "FAILED: " + e.getMessage());
        }
    }
    return results;
}

    // 4. FINALIZE: Moves to CURRENT Employee
    public Employee finalizeEmployee(String empId) {
        // 1. Fetch the temporary Employee record (Status: ONBOARDING)
        Employee emp = employeeRepo.findByEmployeeId(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + empId));

        // 2. Fetch the BGV/Onboarding record using email
        // Logic: Since BGV was approved, this record MUST exist.
        BGVSubmission bgv = bgvRepo.findByEmailAddress(emp.getEmailAddress());
        if (bgv == null) throw new RuntimeException("BGV/Onboarding data missing for: " + emp.getEmailAddress());

        // 3. Fetch the original Job Application
        JobApplication app = careerRepo.findById(bgv.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Job Application not found"));

        // --- MAPPING DATA AUTOMATICALLY TO YOUR EMPLOYEE ENTITY ---

        // A. Basic Information
        emp.setFullName(bgv.getFullName());
        emp.setDateOfBirth(bgv.getDob());
        emp.setPhoneNumber(bgv.getContactNumber());
        emp.setAddress(bgv.getCurrentAddress());
        emp.setGender(app.getGender()); 

        // B. Job Information (Automation from Job Application)
        emp.setDepartment(app.getDepartment());
        emp.setDesignation(app.getCurrentJobTitle());
        emp.setReportingManager(app.getReferenceName()); 
        emp.setEmployeeType("Full Time"); 
        emp.setWorkLocation(app.getCurrentAddress()); 
        emp.setEmploymentStatus(EmployeeStatus.CURRENT); // THIS MAKES THEM AN ACTIVE EMPLOYEE
        emp.setDateOfJoining(java.time.LocalDate.now().toString());

        // C. Payroll & Identity
        emp.setSalary(app.getExpectedSalary()); 
        emp.setBankName(bgv.getBankName());
        emp.setBankAccountNumber(bgv.getAccountNumber());
        emp.setIfscCode(bgv.getIfscCode());
        emp.setPanNumber(bgv.getPanNumber());
        emp.setAadhaarNumber(bgv.getAadharNumber());
        emp.setPassport(bgv.getPassportNumber());

        // D. Emergency Contacts
        emp.setEmergencyContactName(bgv.getEmergencyContactName());
        emp.setEmergencyContactNumber(bgv.getEmergencyContactNumber());

        // E. Document Uploads (Mapping Map keys to Employee fields)
        if (bgv.getDocumentPaths() != null) {
            Map<String, String> docs = bgv.getDocumentPaths();
            emp.setResume(app.getCvFileUrl()); 
            emp.setAadhaarCard(docs.get("AADHAR_CARD"));
            emp.setPanCardDoc(docs.get("PAN_CARD"));
            emp.setOfferLetter(docs.get("OFFER_LETTER"));
            emp.setEducationalCertificates(docs.get("HIGHEST_DEGREE"));
        }

        // 4. Update the Application Stage to HIRED
        app.setStage(ApplicationStage.HIRED);
        careerRepo.save(app);

        // 5. Update BGV Status to COMPLETED
        bgv.setStatus("COMPLETED");
        bgvRepo.save(bgv);

        // 6. SAVE AND RETURN
        return employeeRepo.save(emp);
    }
}