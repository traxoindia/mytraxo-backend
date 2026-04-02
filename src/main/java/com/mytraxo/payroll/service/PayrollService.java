package com.mytraxo.payroll.service;

import com.mytraxo.attendance.repository.AttendanceRepository;
import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.repo.EmployeeRepository;
import com.mytraxo.payroll.entity.PayrollRecord;
import com.mytraxo.payroll.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor // ADD THIS
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    public PayrollRecord generateMonthlyPayroll(String employeeId, int month, int year) {
        Employee emp = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate start = LocalDate.of(year, month, 1);
        int totalDays = start.lengthOfMonth();

        // Count "PRESENT" from your existing attendance collection
        long presentDays = attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, start, start.withDayOfMonth(totalDays))
                .stream()
                .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                .count();

       double monthlyGross = 0.0;
if (emp.getSalary() != null) {
    try {
        monthlyGross = Double.parseDouble(emp.getSalary());
    } catch (NumberFormatException e) {
        monthlyGross = 0.0;
    }
} else if (emp.getMonthlySalary() != null) {
    monthlyGross = emp.getMonthlySalary();
}
        double earnedGross = (monthlyGross / totalDays) * presentDays;

        double basic = earnedGross * 0.50;
        double hra = earnedGross * 0.20;
        double pf = basic * 0.12;
        double tax = (earnedGross > 50000) ? earnedGross * 0.05 : 0.0;
       // double netSalary = earnedGross - (pf + tax + 200); // 200 is Prof Tax
       // To this (only deduct if they actually earned something):
double netSalary = (earnedGross > 200) ? (earnedGross - (pf + tax + 200)) : 0.0;
        PayrollRecord record = PayrollRecord.builder()
                .employeeId(employeeId)
                .employeeName(emp.getFullName())
                .month(month).year(year)
                .totalDaysInMonth(totalDays)
                .presentDays(presentDays)
                .basicPay(round(basic)).hra(round(hra)).grossEarned(round(earnedGross))
                .pfDeduction(round(pf)).taxDeduction(round(tax))
                .netSalary(round(netSalary))
                .paymentStatus("PENDING")
                .generatedAt(LocalDateTime.now())
                .build();

        return payrollRepository.save(record);
    }

    private double round(double v) { return Math.round(v * 100.0) / 100.0; }
    public List<PayrollRecord> getEmployeePayrollHistory(String employeeId) {
    return payrollRepository.findByEmployeeIdOrderByYearDescMonthDesc(employeeId);
}
public PayrollRecord markAsPaid(String payrollId) {
    PayrollRecord record = payrollRepository.findById(payrollId)
            .orElseThrow(() -> new RuntimeException("Payroll record not found"));
    record.setPaymentStatus("PAID");
    return payrollRepository.save(record);
}

public byte[] generatePayslipPdf(String payrollId) {
    PayrollRecord record = payrollRepository.findById(payrollId)
            .orElseThrow(() -> new RuntimeException("Payroll record not found"));

    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // Add Content to PDF
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        document.add(new Paragraph("PAYSLIP - MYTRAXO", titleFont));
        document.add(new Paragraph("--------------------------------------------------"));
        document.add(new Paragraph("Employee Name: " + record.getEmployeeName()));
        document.add(new Paragraph("Employee ID:   " + record.getEmployeeId()));
        document.add(new Paragraph("Month/Year:    " + record.getMonth() + "/" + record.getYear()));
        document.add(new Paragraph("Status:        " + record.getPaymentStatus()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Earnings:"));
        document.add(new Paragraph("Basic Pay:     " + record.getBasicPay()));
        document.add(new Paragraph("HRA:           " + record.getHra()));
        document.add(new Paragraph("Gross Total:   " + record.getGrossEarned()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Deductions:"));
        document.add(new Paragraph("PF:            " + record.getPfDeduction()));
        document.add(new Paragraph("Tax:           " + record.getTaxDeduction()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("NET SALARY:    " + record.getNetSalary(), titleFont));
        document.add(new Paragraph("--------------------------------------------------"));

        document.close();
        return out.toByteArray();
    } catch (Exception e) {
        throw new RuntimeException("Error generating PDF", e);
    }
}
}