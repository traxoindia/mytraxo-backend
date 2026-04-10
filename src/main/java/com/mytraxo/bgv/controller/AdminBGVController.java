package com.mytraxo.bgv.controller;

import com.mytraxo.bgv.service.BGVService;
import lombok.RequiredArgsConstructor;
import com.mytraxo.bgv.repo.BGVRepository;

import com.mytraxo.bgv.entity.BGVSubmission;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/bgv") // All paths start with /api/admin
@RequiredArgsConstructor
public class AdminBGVController {

    private final BGVService bgvService;
    private final BGVRepository bgvRepo;

    @GetMapping("/get-link/{appId}")
public ResponseEntity<Map<String, String>> getBgvLink(@PathVariable String appId) {
    BGVSubmission bgv = bgvRepo.findByApplicationId(appId)
            .orElseThrow(() -> new RuntimeException("BGV not initiated yet"));
    
    String link = "https://mytraxo.com/bgv-portal?token=" + bgv.getToken();
    
    return ResponseEntity.ok(Map.of("link", link));
}

    // Approve the BGV stage and move to Onboarding
    @PostMapping("/{bgvId}/approve-bgv")
    public ResponseEntity<String> approveBGV(@PathVariable String bgvId) {
        bgvService.approveBGVStage(bgvId);
        return ResponseEntity.ok("BGV Approved. Candidate moved to Onboarding stage.");
    }

    // Reject the candidate
    @PostMapping("/{bgvId}/reject")
    public ResponseEntity<String> rejectBGV(@PathVariable String bgvId) {
        bgvService.rejectCandidateProcess(bgvId);
        return ResponseEntity.ok("Candidate has been rejected.");
    }

    // Finalize Onboarding and make them a CURRENT employee
    @PostMapping("/finalize/{empId}")
    public ResponseEntity<String> finalize(@PathVariable String empId) {
        bgvService.finalizeEmployee(empId);
        return ResponseEntity.ok("Onboarding finalized. Employee is now CURRENT.");
    }
}