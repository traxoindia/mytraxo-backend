package com.mytraxo.employee.controller;

import com.mytraxo.employee.dto.EmployeeRequest;
import com.mytraxo.employee.dto.LeaveEmployeeRequest;
import com.mytraxo.employee.dto.ResumeParseResponse;
import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.entity.EmployeeStatus;
import com.mytraxo.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import com.mytraxo.employee.dto.EmployeeNameDTO;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/hr/employees")
@RequiredArgsConstructor
public class HrEmployeeController {

    private final EmployeeService employeeService;

    // 1. Submit Selection Form
    @PostMapping("/select")
    public ResponseEntity<Employee> createSelectedEmployee(@RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.createSelectedEmployee(request));
    }

    // 2. Move Selected -> Onboarding
    @PutMapping("/{employeeId}/move-onboarding")
    public ResponseEntity<Employee> moveToOnboarding(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.moveToOnboarding(employeeId));
    }

    // 3. Move Onboarding -> Current
    @PutMapping("/{employeeId}/mark-current")
    public ResponseEntity<Employee> markAsCurrent(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.markAsCurrent(employeeId));
    }

    @PostMapping(value = "/upload-resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeParseResponse> uploadResume(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(employeeService.parseResume(file));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.getByEmployeeId(employeeId));
    }

    // This handles Selected, Onboarding, Current tabs
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Employee>> getEmployeesByStatus(@PathVariable EmployeeStatus status) {
        return ResponseEntity.ok(employeeService.getByStatus(status));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable String employeeId,
            @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, request));
    }

    @PutMapping("/{employeeId}/mark-left")
    public ResponseEntity<Employee> markAsLeft(
            @PathVariable String employeeId,
            @RequestBody LeaveEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.markAsLeft(employeeId, request));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @GetMapping("/names")
    public ResponseEntity<List<EmployeeNameDTO>> getEmployeeNames() {
        return ResponseEntity.ok(employeeService.getAllEmployeeNames());
    }
}