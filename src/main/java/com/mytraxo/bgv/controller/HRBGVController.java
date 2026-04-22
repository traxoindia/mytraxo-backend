package com.mytraxo.bgv.controller;

import com.mytraxo.bgv.service.BGVService;
import lombok.RequiredArgsConstructor;
import com.mytraxo.bgv.repo.BGVRepository;

import com.mytraxo.bgv.entity.BGVSubmission;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/hr/bgv") // All paths start with /api/hr
@RequiredArgsConstructor
public class HRBGVController {

    private final BGVService bgvService;
    private final BGVRepository bgvRepo;
// This injects the value from your YAML file
    @Value("${app.frontendUrl}")
    private String frontendUrl;

    @GetMapping("/get-link/{appId}")
    public ResponseEntity<Map<String, String>> getBgvLink(@PathVariable String appId) {
        BGVSubmission bgv = bgvRepo.findByApplicationId(appId)
                .orElseThrow(() -> new RuntimeException("BGV not initiated yet"));
        
        // Use the variable here to build the link
        String link = frontendUrl + "/bgv-portal?token=" + bgv.getToken();
        
        return ResponseEntity.ok(Map.of("link", link));
    }
// Endpoint for the frontend to fetch candidate name/email/status
    @GetMapping("/get-details/{token}")
    public ResponseEntity<BGVSubmission> getBgvDetails(@PathVariable String token) {
        // We call the service to get the data
        BGVSubmission submission = bgvService.getDetailsByToken(token);
        
        // We return the whole object (Frontend will pick what it needs)
        return ResponseEntity.ok(submission);
    }
    @GetMapping("/list")
public ResponseEntity<List<BGVSubmission>> getAllBgvApplications() {
    return ResponseEntity.ok(bgvRepo.findAll());
}
        @GetMapping("/pending-approval")
public ResponseEntity<List<BGVSubmission>> getPendingApprovals() {
    return ResponseEntity.ok(bgvRepo.findByStatus("BGV_SUBMITTED"));
}

    // Approve the BGV stage and move to Onboarding
   @PostMapping("/{bgvId}/approve-bgv")
public ResponseEntity<String> approveBGV(@PathVariable String bgvId) {
    // 1. Service now returns the generated Employee ID
    String employeeId = bgvService.approveBGVStage(bgvId); 
    
    // 2. Return a message that includes the ID so HR knows the onboarding link
    return ResponseEntity.ok("Approved! Candidate moved to onboarding. ID: " + employeeId);
}

    // Reject the candidate
    @PostMapping("/{bgvId}/reject")
    public ResponseEntity<String> rejectBGV(@PathVariable String bgvId) {
        bgvService.rejectCandidateProcess(bgvId);
        return ResponseEntity.ok("Candidate has been rejected.");
    }
     /**
     * BULK APPROVE BGV
     * Frontend sends a list of BGV IDs.
     * This moves them from "BGV Submitted" to "Onboarding" status and creates Employee records.
     */
    @PostMapping("/bulk-approve")
    public ResponseEntity<Map<String, String>> bulkApprove(@RequestBody List<String> bgvIds) {
        Map<String, String> results = new java.util.HashMap<>();
        for (String id : bgvIds) {
            try {
                bgvService.approveBGVStage(id);
                results.put(id, "Approved and moved to Onboarding");
            } catch (Exception e) {
                results.put(id, "Error: " + e.getMessage());
            }
        }
        return ResponseEntity.ok(results);
    }

// 2. Get candidates who SUBMITTED ONBOARDING (Ready for HR Bulk Finalize)
@GetMapping("/ready-to-finalize")
public ResponseEntity<List<BGVSubmission>> getReadyToFinalize() {
    return ResponseEntity.ok(bgvRepo.findByStatus("ONBOARDING_SUBMITTED"));
}


    // Finalize Onboarding and make them a CURRENT employee
    @PostMapping("/finalize/{empId}")
    public ResponseEntity<String> finalize(@PathVariable String empId) {
        bgvService.finalizeEmployee(empId);
        return ResponseEntity.ok("Onboarding finalized. Employee is now CURRENT.");
    }
        @PostMapping("/bulk-finalize")
    public ResponseEntity<Map<String, String>> bulkFinalize(@RequestBody List<String> empIds) {
        Map<String, String> results = bgvService.bulkFinalizeEmployees(empIds);
        return ResponseEntity.ok(results);
    }

}