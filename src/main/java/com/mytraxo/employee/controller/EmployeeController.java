package com.mytraxo.employee.controller;

import com.mytraxo.auth.dto.EmployeeProfileDTO;
import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.repo.EmployeeRepository;

import com.mytraxo.employee.service.EmployeeService;

import com.mytraxo.employee.dto.EmployeeRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepo;
     private final EmployeeService employeeService;
    public EmployeeController(EmployeeRepository employeeRepo,EmployeeService employeeService) { this.employeeRepo = employeeRepo; this.employeeService = employeeService; }

    @GetMapping("/directory")
    public ResponseEntity<List<EmployeeProfileDTO>> getDirectory() {
        // Fetch all employees and map to safe DTOs
        List<EmployeeProfileDTO> directory = employeeRepo.findAll().stream()
            .map(emp -> new EmployeeProfileDTO(
                emp.getEmployeeId(),
                emp.getFullName(),
                emp.getEmailAddress(),
                emp.getPhoneNumber(),
                emp.getDesignation(),
                null // Profile pic can be added here
            )).collect(Collectors.toList());

        return ResponseEntity.ok(directory);
    }
    @GetMapping("/profile/{employeeId}")
public ResponseEntity<Employee> getProfile(@PathVariable String employeeId) {
    // Calling the service method to find employee by ID
    return ResponseEntity.ok(employeeService.getEmployeeById(employeeId)); 
}
    // This is the endpoint the mobile app will call to update a profile
    @PutMapping("/update/{employeeId}")
    public ResponseEntity<Employee> updateProfile(@PathVariable String employeeId, @RequestBody EmployeeRequest request) {
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(updatedEmployee);
    }
}