package com.mytraxo.bgv.controller;

import org.springframework.http.ResponseEntity;
import com.mytraxo.bgv.entity.BGVSubmission;
import com.mytraxo.bgv.repo.BGVRepository;

import com.mytraxo.employee.entity.Employee;

import com.mytraxo.employee.repo.EmployeeRepository;
import com.mytraxo.bgv.service.BGVService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import com.mytraxo.bgv.dto.OnboardingRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/bgv")
public class PublicBGVController {

   
   private final BGVRepository bgvRepo;
    private final EmployeeRepository employeeRepository;
    private final BGVService bgvService; // Injected the Service
    private final String UPLOAD_DIR = "uploads/bgv_docs/";

    // PAGE 1: Background Verification Details
@PostMapping(value = "/submit-verification", consumes = "multipart/form-data")
    public String saveVerification(
            @RequestParam String token, 
            @ModelAttribute BGVSubmission data, // Maps JSON fields from form-data
            @RequestParam("aadharCard") MultipartFile aadharCard,
            @RequestParam("panCard") MultipartFile panCard,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam(value = "marksheet10", required = false) MultipartFile marksheet10,
            @RequestParam(value = "marksheet12", required = false) MultipartFile marksheet12,
            @RequestParam(value = "degree", required = false) MultipartFile degree           // Optional
) {
        BGVSubmission bgv = bgvRepo.findByToken(token).orElseThrow();
        bgv.setFullName(data.getFullName());
    bgv.setDob(data.getDob());
    bgv.setContactNumber(data.getContactNumber());
    bgv.setAadharNumber(data.getAadharNumber());
    bgv.setPanNumber(data.getPanNumber());
    bgv.setLastDrawnSalary(data.getLastDrawnSalary());
    bgv.setCriminalRecordDeclaration(data.getCriminalRecordDeclaration());
    bgv.setEducationDetails(data.getEducationDetails());
    bgv.setEmploymentHistory(data.getEmploymentHistory());
    bgv.setReferences(data.getReferences());
        // Delegate all saving logic to the service to avoid redundancy
        bgvService.submitVerificationWithDocs(
                token, data, aadharCard, panCard, photo, marksheet10, marksheet12, degree
        );
        bgv.setStatus("BGV_SUBMITTED");
        bgvRepo.save(bgv);
        return "Verification details saved. Proceed to Onboarding Page.";
    }


    // PAGE 2: Onboarding & Bank Details
    @PostMapping("/submit-onboarding/{bgvId}")
public ResponseEntity<?> submitOnboarding(@PathVariable String bgvId, @RequestBody OnboardingRequest request) {
    
    BGVSubmission bgv = bgvRepo.findById(bgvId)
            .orElseThrow(() -> new RuntimeException("Invalid BGV ID"));

    // Save Bank & Emergency info into the BGV record instead of Employee
    bgv.setBankName(request.getBankName());
    bgv.setAccountNumber(request.getAccountNumber());
    bgv.setIfscCode(request.getIfscCode());
    bgv.setEmergencyContactName(request.getEmergencyContactName());
    bgv.setEmergencyContactNumber(request.getEmergencyContactNumber());
    
    bgv.setStatus("ONBOARDING_SUBMITTED");
    bgvRepo.save(bgv);

    return ResponseEntity.ok("Onboarding details saved");
}
    

    // FILE UPLOAD HANDLER
    @PostMapping("/upload-docs")
public ResponseEntity<?> handleFileUpload(
        @RequestParam String token,
        @RequestParam String documentType, // e.g., "Last 3 Months Salary Slips"
        @RequestParam("file") MultipartFile file) {
    
    try {
        // 1. Define folder: uploads/bgv/{token}/
        String uploadDir = "uploads/bgv/" + token + "/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // 2. Save file
        String fileName = documentType.replaceAll(" ", "_") + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 3. Update BGV Record in Mongo
        BGVSubmission bgv = bgvRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));
                if (bgv.getDocumentPaths() == null) {
    bgv.setDocumentPaths(new java.util.HashMap<>());
}
        
        bgv.getDocumentPaths().put(documentType, filePath.toString());
        bgvRepo.save(bgv);

        return ResponseEntity.ok("Uploaded: " + documentType);
    } catch (IOException e) {
        return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
    }
}
}