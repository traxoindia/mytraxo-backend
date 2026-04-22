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
import com.mytraxo.attendance.dto.CheckInResponse;

import com.mytraxo.attendance.dto.MobileCheckInRequest;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import com.mytraxo.attendance.entity.Notification; 

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

     // ✅ AUTOMATIC CHECK-IN
    // Frontend body: { "lat": 22.5, "lng": 88.3 }
  
@PostMapping("/check-in")
public Attendance checkIn(java.security.Principal principal, @RequestBody CheckInRequest request) {
    String email = principal.getName(); 
    return service.checkIn(email, request.getLat(), request.getLng());
}

// --- ADD THIS FOR MOBILE (New Path) ---
@PostMapping("/mobile/check-in")
public ResponseEntity<?> mobileCheckIn(@RequestBody MobileCheckInRequest request) {
    try {
        // Now returns CheckInResponse
        CheckInResponse response = service.mobileCheckInProcess(request);
        return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
        // This is where "Location mismatch" will show up if it fails
        return ResponseEntity.badRequest().body(e.getMessage());
    }
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
 @GetMapping("/notifications/{employeeId}")
    public List<Notification> getNotifications(@PathVariable String employeeId) {
        return service.getNotificationsForEmployee(employeeId);
    }
}