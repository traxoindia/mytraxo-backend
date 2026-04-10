package com.mytraxo.employee.service;

import com.mytraxo.employee.dto.EmployeeRequest;
import com.mytraxo.employee.dto.LeaveEmployeeRequest;
import com.mytraxo.employee.dto.ResumeParseResponse;
import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.entity.EmployeeStatus;
import com.mytraxo.employee.repo.EmployeeRepository;
import com.mytraxo.employee.util.ResumeParserUtil;
import lombok.RequiredArgsConstructor;
import com.mytraxo.employee.dto.EmployeeNameDTO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ResumeParserUtil resumeParserUtil;

    // 1. Initial State: Create as SELECTED
    public Employee createSelectedEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }

        Employee employee = mapRequestToEmployee(request);
        employee.setEmploymentStatus(EmployeeStatus.SELECTED); // Changed status to SELECTED

        return employeeRepository.save(employee);
    }

    // 2. Transition: SELECTED -> ONBOARDING
    public Employee moveToOnboarding(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setEmploymentStatus(EmployeeStatus.ONBOARDING);
        return employeeRepository.save(employee);
    }

    // 3. Transition: ONBOARDING -> CURRENT
    public Employee markAsCurrent(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setEmploymentStatus(EmployeeStatus.CURRENT);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(String employeeId, EmployeeRequest request) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        employee.setRole(request.getRole());
        employee.setFullName(request.getFullName());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setGender(request.getGender());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setEmailAddress(request.getEmailAddress());
        employee.setAddress(request.getAddress());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());
        employee.setReportingManager(request.getReportingManager());
        employee.setEmployeeType(request.getEmployeeType());
        employee.setDateOfJoining(request.getDateOfJoining());
        employee.setWorkLocation(request.getWorkLocation());
        employee.setSalary(request.getSalary());
        employee.setBankAccountNumber(request.getBankAccountNumber());
        employee.setBankName(request.getBankName());
        employee.setIfscCode(request.getIfscCode());
        employee.setPanNumber(request.getPanNumber());
        employee.setAadhaarNumber(request.getAadhaarNumber());
        employee.setPanCard(request.getPanCard());
        employee.setPassport(request.getPassport());
        employee.setEmergencyContactName(request.getEmergencyContactName());
        employee.setEmergencyContactNumber(request.getEmergencyContactNumber());
        employee.setEducationQualification(request.getEducationQualification());
        employee.setPreviousWorkExperience(request.getPreviousWorkExperience());
        employee.setResume(request.getResume());
        employee.setAadhaarCard(request.getAadhaarCard());
        employee.setPanCardDoc(request.getPanCardDoc());
        employee.setOfferLetter(request.getOfferLetter());
        employee.setEducationalCertificates(request.getEducationalCertificates());
        employee.setMonthlySalary(request.getMonthlySalary());

        return employeeRepository.save(employee);
    }

    public Employee markAsLeft(String employeeId, LeaveEmployeeRequest request) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setEmploymentStatus(EmployeeStatus.LEFT);
        employee.setLeavingDate(request.getLeavingDate());
        employee.setLeavingReason(request.getLeavingReason());

        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<Employee> getByStatus(EmployeeStatus status) {
        return employeeRepository.findByEmploymentStatus(status);
    }

    public void deleteEmployee(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employeeRepository.delete(employee);
    }

    public ResumeParseResponse parseResume(MultipartFile file) {
        return resumeParserUtil.parseResume(file);
    }

    private Employee mapRequestToEmployee(EmployeeRequest request) {
        return Employee.builder()
                .role(request.getRole())
                .employeeId(request.getEmployeeId())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .emailAddress(request.getEmailAddress())
                .address(request.getAddress())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .reportingManager(request.getReportingManager())
                .employeeType(request.getEmployeeType())
                .dateOfJoining(request.getDateOfJoining())
                .workLocation(request.getWorkLocation())
                .salary(request.getSalary())
                .bankAccountNumber(request.getBankAccountNumber())
                .bankName(request.getBankName())
                .ifscCode(request.getIfscCode())
                .panNumber(request.getPanNumber())
                .aadhaarNumber(request.getAadhaarNumber())
                .panCard(request.getPanCard())
                .passport(request.getPassport())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactNumber(request.getEmergencyContactNumber())
                .educationQualification(request.getEducationQualification())
                .previousWorkExperience(request.getPreviousWorkExperience())
                .resume(request.getResume())
                .aadhaarCard(request.getAadhaarCard())
                .panCardDoc(request.getPanCardDoc())
                .offerLetter(request.getOfferLetter())
                .educationalCertificates(request.getEducationalCertificates())
                .monthlySalary(request.getMonthlySalary())
                .build();
    }

    public List<EmployeeNameDTO> getAllEmployeeNames() {
        return employeeRepository.findAll().stream()
                .map(emp -> new EmployeeNameDTO(
                        emp.getEmployeeId(),
                        emp.getFullName()
                ))
                .collect(Collectors.toList());
    }
}