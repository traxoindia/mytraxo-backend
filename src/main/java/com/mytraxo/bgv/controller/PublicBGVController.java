package com.mytraxo.bgv.controller;

import org.springframework.http.ResponseEntity;
import com.mytraxo.bgv.entity.BGVSubmission;
import com.mytraxo.bgv.repo.BGVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@RestController
@RequestMapping("/api/public/bgv")
public class PublicBGVController {

    @Autowired private BGVRepository bgvRepo;
    private final String UPLOAD_DIR = "uploads/bgv_docs/";

    // PAGE 1: Background Verification Details
    @PostMapping("/submit-verification")
    public String saveVerification(@RequestParam String token, @RequestBody BGVSubmission data) {
        BGVSubmission bgv = bgvRepo.findByToken(token).orElseThrow();
        bgv.setFullName(data.getFullName());
        bgv.setDob(data.getDob());
        bgv.setEducationDetails(data.getEducationDetails());
        bgv.setEmploymentHistory(data.getEmploymentHistory());
        bgv.setStatus("BGV_SUBMITTED");
        bgvRepo.save(bgv);
        return "Verification details saved. Proceed to Onboarding Page.";
    }

    // PAGE 2: Onboarding & Bank Details
    @PostMapping("/submit-onboarding")
    public String saveOnboarding(@RequestParam String token, @RequestBody BGVSubmission data) {
        BGVSubmission bgv = bgvRepo.findByToken(token).orElseThrow();
        bgv.setBankName(data.getBankName());
        bgv.setAccountNumber(data.getAccountNumber());
        bgv.setIfscCode(data.getIfscCode());
        bgv.setCurrentAddress(data.getCurrentAddress());
        bgv.setStatus("SUBMITTED"); // Final submission status
        bgvRepo.save(bgv);
        return "All details submitted successfully. HR will review.";
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