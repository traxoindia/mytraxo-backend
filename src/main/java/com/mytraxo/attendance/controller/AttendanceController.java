package com.mytraxo.attendance.controller;

import com.mytraxo.attendance.entity.Attendance;
import com.mytraxo.attendance.service.AttendanceService;
import com.mytraxo.attendance.dto.AttendanceReportDto;
import com.mytraxo.attendance.dto.CalendarDto;
import com.mytraxo.attendance.dto.CheckInRequest;
import com.mytraxo.attendance.dto.CheckOutRequest;
import com.mytraxo.attendance.dto.QRRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    // ✅ 1. CHECK-IN (JSON BODY)
    @PostMapping("/check-in")
    public Attendance checkIn(@RequestBody CheckInRequest request) {

        return service.checkIn(
                request.getEmployeeId(),
                request.getEmployeeName(),
                request.getLat(),
                request.getLng()
        );
    }

    // ✅ 2. CHECK-OUT (JSON BODY)
    @PostMapping("/check-out")
    public Attendance checkOut(@RequestBody CheckOutRequest request) {

        return service.checkOut(request.getEmployeeId());
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
}