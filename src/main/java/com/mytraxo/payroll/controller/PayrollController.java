package com.mytraxo.payroll.controller;

import com.mytraxo.payroll.dto.SettlementRequest;
import com.mytraxo.payroll.entity.PayrollRecord;
import com.mytraxo.payroll.entity.SettlementRecord;
import com.mytraxo.payroll.service.PayrollService;
import com.mytraxo.payroll.service.SettlementService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.repo.EmployeeRepository;
import com.mytraxo.employee.entity.EmployeeStatus; 

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor; // ✅ ADD THIS

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payroll")
public class PayrollController {

     private final PayrollService payrollService;
      private final SettlementService settlementService;
      private final EmployeeRepository employeeRepository; 


    @PostMapping("/generate/{empId}/{month}/{year}")
    public ResponseEntity<PayrollRecord> generate(@PathVariable String empId, @PathVariable int month, @PathVariable int year) {
        return ResponseEntity.ok(payrollService.generateMonthlyPayroll(empId, month, year));
    }

    @PostMapping("/settlement")
    public ResponseEntity<SettlementRecord> settle(@RequestBody SettlementRequest request) {
        return ResponseEntity.ok(settlementService.processSettlement(
                request.getEmployeeId(), 
                request.getLwd(), 
                request.getUnusedLeaves(), 
                request.isNoticeServed()));
    }
    
    @GetMapping("/my-payslips")
    public ResponseEntity<List<PayrollRecord>> getMyPayslips(Authentication auth) {
        // authentication is automatically injected by Spring
        
        // 3. Extract the Email from the Token
        String email = auth.getName(); 

        // 4. Find the actual Employee ID using the Email
        Employee employee = employeeRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Employee record not found for: " + email));

        // 5. Use the actual Employee ID (e.g. "EMP001") to fetch history
        List<PayrollRecord> payslips = payrollService.getEmployeePayrollHistory(employee.getEmployeeId());
        
        return ResponseEntity.ok(payslips);
    }
      // 1. Mark salary as transferred (Status: PAID)
    @PutMapping("/pay/{payrollId}")
    public ResponseEntity<PayrollRecord> markPaid(@PathVariable String payrollId) {
        return ResponseEntity.ok(payrollService.markAsPaid(payrollId));
    }

    // 2. Download Payslip PDF
    @GetMapping("/download/{payrollId}")
    public ResponseEntity<byte[]> downloadPayslip(@PathVariable String payrollId) {
        byte[] pdfContents = payrollService.generatePayslipPdf(payrollId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // This makes the browser download the file as "payslip.pdf"
        headers.setContentDispositionFormData("attachment", "payslip_" + payrollId + ".pdf");
        
        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
      @GetMapping("/history/{empId}")
    public ResponseEntity<List<PayrollRecord>> getEmployeeHistory(@PathVariable String empId) {
        // This calls the service method we created earlier
        List<PayrollRecord> history = payrollService.getEmployeePayrollHistory(empId);
        return ResponseEntity.ok(history);
    }
    @PostMapping("/generate-bulk/{month}/{year}")
    public ResponseEntity<String> generateBulk(@PathVariable int month, @PathVariable int year) {
        // 1. Fetch all employees who are currently ACTIVE
        List<Employee> activeEmployees = employeeRepository.findByEmploymentStatus(EmployeeStatus.ACTIVE);
        
        int successCount = 0;
        int errorCount = 0;

        for (Employee emp : activeEmployees) {
            try {
                // 2. Reuse your existing single-generate logic
                payrollService.generateMonthlyPayroll(emp.getEmployeeId(), month, year);
                successCount++;
            } catch (Exception e) {
                // If one employee fails (e.g., missing salary), skip them and continue
                errorCount++;
                System.err.println("Failed to generate payroll for: " + emp.getEmployeeId() + " Error: " + e.getMessage());
            }
        }

        return ResponseEntity.ok("Successfully generated payroll for " + successCount + " employees. " +
                                (errorCount > 0 ? "Failed for " + errorCount + " employees." : ""));
    }
}