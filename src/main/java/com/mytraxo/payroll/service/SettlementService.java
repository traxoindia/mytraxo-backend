package com.mytraxo.payroll.service;

import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.entity.EmployeeStatus; // Import the Enum
import com.mytraxo.employee.repo.EmployeeRepository;
import com.mytraxo.payroll.entity.PayrollRecord;
import com.mytraxo.payroll.entity.SettlementRecord;
import com.mytraxo.payroll.repository.SettlementRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollService payrollService;

    public SettlementRecord processSettlement(String empId, LocalDate lwd, int leaves, boolean noticeServed) {
        Employee emp = employeeRepository.findByEmployeeId(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        PayrollRecord finalPay = payrollService.generateMonthlyPayroll(empId, lwd.getMonthValue(), lwd.getYear());

        double monthlySalary = emp.getMonthlySalary() != null ? emp.getMonthlySalary() : 0.0;
        double dailyRate = monthlySalary / 30;
        double leaveEncashment = dailyRate * leaves;
        double noticeRecovery = noticeServed ? 0.0 : monthlySalary;
        double netFinal = finalPay.getNetSalary() + leaveEncashment - noticeRecovery;

        SettlementRecord settlement = SettlementRecord.builder()
                .employeeId(empId)
                .employeeName(emp.getFullName())
                .lastWorkingDay(lwd)
                .finalMonthSalary(finalPay.getNetSalary())
                .leaveEncashment(leaveEncashment)
                .noticeRecovery(noticeRecovery)
                .netSettlementAmount(netFinal)
                .status("PROCESSED")
                .processedAt(LocalDateTime.now())
                .build();

        // FIX: Use the correct method name and the Enum type
        emp.setEmploymentStatus(com.mytraxo.employee.entity.EmployeeStatus.LEFT); // Correct Enum usage
        emp.setExitDate(lwd);
        employeeRepository.save(emp);

        return settlementRepository.save(settlement);
    }
}