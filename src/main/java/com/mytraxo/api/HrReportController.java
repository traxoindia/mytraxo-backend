package com.mytraxo.api;

import com.mytraxo.api.dto.ReportRequest;
import com.mytraxo.api.service.ReportService;
import com.mytraxo.attendance.entity.Attendance;
import com.mytraxo.leave.entity.Leave;
import com.mytraxo.payroll.entity.PayrollRecord;
import com.mytraxo.employee.entity.Employee;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor; // ✅ ADD THIS IMPORT
import java.util.List;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/hr/reports")
@CrossOrigin("*")
public class HrReportController {


   private final ReportService reportService;

   // 1. Attendance Report by Employee ID (with optional Date/Month/Year params)
    // Example: /api/hr/reports/attendance/EMP101?year=2024&month=10
    @GetMapping("/attendance/{employeeId}")
    public List<Attendance> getAttendanceByEmployee(
            @PathVariable String employeeId, 
            @ModelAttribute ReportRequest request) {
        
        request.setEmployeeId(employeeId); // Force the ID from path into the filter
        return reportService.getWiseReport(request, Attendance.class, "date");
    }

    // 2. Payroll Report by Employee ID
    // Example: /api/hr/reports/payroll/EMP101?year=2024
    @GetMapping("/payroll/{employeeId}")
    public List<PayrollRecord> getPayrollByEmployee(
            @PathVariable String employeeId, 
            @ModelAttribute ReportRequest request) {
            
        request.setEmployeeId(employeeId);
        return reportService.getWiseReport(request, PayrollRecord.class, "date");
    }

    // 3. Leave Report by Employee ID
    // Example: /api/hr/reports/leave/EMP101?month=5&year=2024
    @GetMapping("/leave/{employeeId}")
    public List<Leave> getLeaveByEmployee(
            @PathVariable String employeeId, 
            @ModelAttribute ReportRequest request) {
            
        request.setEmployeeId(employeeId);
        return reportService.getWiseReport(request, Leave.class, "fromDate");
    }

    // 4. Hiring Report (Usually search by Name/Date for all)
    @GetMapping("/new-hire")
    public List<Employee> hiringReport(@ModelAttribute ReportRequest request) {
        // We change "joiningDate" to "dateOfJoining" to match your entity
        return reportService.getWiseReport(request, Employee.class, "dateOfJoining");
    }

    // 5. General Employee Report
    @GetMapping("/employee-summary")
    public List<Employee> employeeSummary(@ModelAttribute ReportRequest request) {
        return reportService.getWiseReport(request, Employee.class, "createdAt");
    }
    @GetMapping("/leave/all")
public List<Leave> getAllLeaveReports(@ModelAttribute ReportRequest request) {
    return reportService.getWiseReport(request, Leave.class, "fromDate");
}
}