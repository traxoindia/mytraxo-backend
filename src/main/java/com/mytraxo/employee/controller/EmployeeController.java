package com.mytraxo.employee.controller;

import com.mytraxo.auth.dto.EmployeeProfileDTO;
import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.repo.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepo;
    public EmployeeController(EmployeeRepository employeeRepo) { this.employeeRepo = employeeRepo; }

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
}