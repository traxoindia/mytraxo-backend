package com.mytraxo.attendance.controller;

import com.mytraxo.attendance.entity.Attendance;
import com.mytraxo.attendance.service.AttendanceService;
import com.mytraxo.attendance.dto.AttendanceReportDto;
import com.mytraxo.attendance.dto.AttendanceStatusDto;
import com.mytraxo.attendance.dto.CalendarDto;
import com.mytraxo.attendance.dto.CheckInRequest;
import com.mytraxo.attendance.dto.CheckOutRequest;
import com.mytraxo.attendance.dto.QRRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

     // ✅ AUTOMATIC CHECK-IN
    // Frontend body: { "lat": 22.5, "lng": 88.3 }
  @PostMapping("/check-in")
// Changed LocationRequest to CheckInRequest
public Attendance checkIn(java.security.Principal principal, @RequestBody CheckInRequest request) {
    
    // 1. Automatic Fetch: Get email from the Token
    String email = principal.getName(); 
    
    // 2. Call Service with email and coordinates from your DTO
    return service.checkIn(email, request.getLat(), request.getLng());
}

@PostMapping("/check-out")
public Attendance checkOut(java.security.Principal principal) {
    // 1. Automatic Fetch: Get email from the Token
    String email = principal.getName();
    
    // 2. No request body needed for check-out
    return service.checkOut(email);
}


    // ✅ 3. GET ATTENDANCE BY EMPLOYEE
    @GetMapping("/{employeeId}")
    public List<Attendance> getByEmployee(@PathVariable String employeeId) {
        return service.getByEmployee(employeeId);
    }

    // ✅ 4. MONTHLY REPORT
    @GetMapping("/report")
    public AttendanceReportDto getMonthlyReport(
            @RequestParam String employeeId,
            @RequestParam int year,
            @RequestParam int month) {

        return service.getMonthlyReport(employeeId, year, month);
    }

    // ✅ 5. CALENDAR API
    @GetMapping("/calendar")
    public List<CalendarDto> getCalendar(
            @RequestParam String employeeId,
            @RequestParam int year,
            @RequestParam int month) {

        return service.getCalendar(employeeId, year, month);
    }

    // ✅ 6. QR CHECK-IN
    @PostMapping("/check-in-qr")
    public Attendance checkInWithQR(@RequestBody QRRequest request) {

        return service.checkInWithQR(
                request.getEmployeeId(),
                request.getQrToken()
        );
    }
    @GetMapping("/hr/all-status")
// Restrict to HR and ADMIN
public List<AttendanceStatusDto> getAllStatusForHR(
        @RequestParam(value = "date", required = false) 
        @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) 
        LocalDate date) {
    
    // Default to today if no date is provided
    LocalDate targetDate = (date != null) ? date : LocalDate.now();
    return service.getAllEmployeesAttendanceStatus(targetDate);
}
}