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

    // New Hire form submit
    @PostMapping("/new-hire")
    public ResponseEntity<Employee> createNewHire(@RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.createNewHire(request));
    }

    // Auto-fill from CV upload
    @PostMapping(value = "/upload-resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeParseResponse> uploadResume(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(employeeService.parseResume(file));
    }

    // Get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // Get employee by employeeId
    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.getByEmployeeId(employeeId));
    }

    // 3 tabs
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Employee>> getEmployeesByStatus(@PathVariable EmployeeStatus status) {
        return ResponseEntity.ok(employeeService.getByStatus(status));
    }

    // Update employee
    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable String employeeId,
            @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, request));
    }

    // Move New Hire -> Current
    @PutMapping("/{employeeId}/mark-current")
    public ResponseEntity<Employee> markAsCurrent(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.markAsCurrent(employeeId));
    }

    // Mark employee as Left
    @PutMapping("/{employeeId}/mark-left")
    public ResponseEntity<Employee> markAsLeft(
            @PathVariable String employeeId,
            @RequestBody LeaveEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.markAsLeft(employeeId, request));
    }

    // Delete employee
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