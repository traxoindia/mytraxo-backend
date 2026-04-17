package com.mytraxo.leave.controller;

import com.mytraxo.leave.entity.Leave;
import com.mytraxo.leave.service.LeaveService;
import lombok.RequiredArgsConstructor;
// Add these to your existing LeaveController.java
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService service;

    // ✅ Apply
    @PostMapping("/apply")
    public Leave apply(@RequestBody Leave leave) {
        return service.applyLeave(leave);
    }

    // ✅ Approve
    @PutMapping("/{id}/approve")
    public Leave approve(@PathVariable String id) {
        return service.approve(id);
    }

    // ✅ Reject
    @PutMapping("/{id}/reject")
    public Leave reject(@PathVariable String id) {
        return service.reject(id);
    }
    // ✅ Get ALL leaves (For Admin/HR)
@GetMapping("/all")
public List<Leave> getAll() {
    return service.getAllLeaves();
}

// ✅ Get PENDING leaves (For Admin/HR to take action quickly)
@GetMapping("/pending")
public List<Leave> getPending() {
    return service.getPendingLeaves();
}

// ✅ Get leaves by Employee ID (For Employee's own dashboard)
@GetMapping("/employee/{employeeId}")
public List<Leave> getByEmployee(@PathVariable String employeeId) {
    return service.getLeavesByEmployee(employeeId);
}
// ✅ NEW: MOBILE SPECIFIC APPLY (Does not affect Web)
    @PostMapping(value = "/mobile/apply", consumes = {"multipart/form-data"})
    public ResponseEntity<?> applyMobile(
            @RequestPart("leaveData") String leaveDataJson, 
            @RequestPart(value = "document", required = false) MultipartFile file) {
        
        try {
            // 1. Convert JSON string to Leave object
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            Leave leave = mapper.readValue(leaveDataJson, Leave.class);

            // 2. Handle File Upload logic (S3 or Local storage)
            String filePath = null;
            if (file != null && !file.isEmpty()) {
                filePath = "uploads/leaves/" + file.getOriginalFilename(); 
                // Save file logic here...
            }

            // 3. Process Leave
            Leave saved = service.applyLeaveMobile(leave, filePath);

            // 4. Return Success status for mobile app
            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Leave applied successfully!",
                "data", saved
            ));

        } catch (Exception e) {
            // Return Error status for mobile app
            return ResponseEntity.status(400).body(Map.of(
                "status", "ERROR",
                "message", "Error applying leave: " + e.getMessage()
            ));
        }
    }
}
