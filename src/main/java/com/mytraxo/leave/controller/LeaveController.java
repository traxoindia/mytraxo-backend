package com.mytraxo.leave.controller;

import com.mytraxo.leave.entity.Leave;
import com.mytraxo.leave.service.LeaveService;
import lombok.RequiredArgsConstructor;

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
}