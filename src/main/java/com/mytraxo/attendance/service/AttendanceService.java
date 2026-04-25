package com.mytraxo.attendance.service;

import com.mytraxo.attendance.entity.Attendance;
import com.mytraxo.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import com.mytraxo.attendance.dto.CheckInResponse;

import com.mytraxo.attendance.dto.MobileCheckInRequest;

import com.mytraxo.attendance.dto.CheckInRequest;

import com.mytraxo.holiday.service.HolidayService;

import com.mytraxo.employee.entity.Employee;

import com.mytraxo.employee.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import com.mytraxo.attendance.dto.AttendanceReportDto;
import com.mytraxo.attendance.dto.AttendanceStatusDto;
import com.mytraxo.attendance.dto.CalendarDto;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.time.*;
import java.util.List;
import com.mytraxo.attendance.entity.Notification;
import com.mytraxo.attendance.repository.NotificationRepository;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository repository;
    private final EmployeeRepository employeeRepository;
    // Inject HolidayService at the top
    private final HolidayService holidayService; 
     @Qualifier("attendanceNotificationRepo") 
    private final NotificationRepository notificationRepository; // This is for Mobile
    private final com.mytraxo.notification.service.NotificationService notificationService;

    // ⏰ Late after 10:15 AM
    private static final LocalTime LATE_TIME = LocalTime.of(10, 15);
    private static final LocalTime HALF_DAY_TIME = LocalTime.of(17, 0);
    private static final double OFFICE_LAT = 21.485979081333724;   // example (update with your office)
    private static final double OFFICE_LNG = 86.90728760427798;
    private static final double ALLOWED_RADIUS = 40; // meters

     public List<AttendanceStatusDto> getAllEmployeesAttendanceStatus(LocalDate date) {
        List<Employee> allEmployees = employeeRepository.findAll();
        List<Attendance> attendanceRecords = repository.findByDate(date);
        
        boolean isHoliday = holidayService.isHoliday(date);

        java.util.Map<String, Attendance> attendanceMap = attendanceRecords.stream()
                .collect(Collectors.toMap(Attendance::getEmployeeId, a -> a, (existing, replacement) -> existing));

        return allEmployees.stream().map(emp -> {
            AttendanceStatusDto dto = new AttendanceStatusDto();
            
            // 1. Set Basic Info
            dto.setEmployeeId(String.valueOf(emp.getEmployeeId()));
            dto.setFullName(emp.getFullName());
            dto.setDepartment(emp.getDepartment());

            Attendance att = attendanceMap.get(String.valueOf(emp.getEmployeeId()));
            
            // 2. Set Status Logic
            if (att != null) {
                if (att.getCheckIn() != null) dto.setCheckInTime(att.getCheckIn().toLocalTime());
                if (att.getCheckOut() != null) dto.setCheckOutTime(att.getCheckOut().toLocalTime());
                dto.setStatus(att.getStatus());
            } else {
                // Determine why they are missing
                if (isHoliday) {
                    dto.setStatus("HOLIDAY");
                } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    dto.setStatus("WEEKEND");
                } else {
                    dto.setStatus("ABSENT");
                }
                dto.setCheckInTime(null);
                dto.setCheckOutTime(null);
            }
            return dto;
        }).collect(Collectors.toList());
    }
  // Inside AttendanceService.java
public CheckInResponse mobileCheckInProcess(MobileCheckInRequest request) {
    String empId = request.getEmployeeId();
    LocalDate today = LocalDate.now();

    Employee employee = employeeRepository.findByEmployeeId(empId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    String workLoc = employee.getWorkLocation();
    boolean isRemote = "REMOTE".equalsIgnoreCase(workLoc) || "WFH".equalsIgnoreCase(workLoc);

    if (repository.findByEmployeeIdAndDate(empId, today).isPresent()) {
        throw new RuntimeException("Already checked in today");
    }

    String message;
    if (isRemote) {
        message = "Remote Check-in Successful";
    } else {
        double distance = calculateDistance(request.getLat(), request.getLng(), OFFICE_LAT, OFFICE_LNG);
        if (distance > ALLOWED_RADIUS) {
            throw new RuntimeException("Location mismatch: You must be at the office.");
        }
        message = "Office Check-in Successful";
    }

    Attendance attendance = new Attendance();
    attendance.setEmployeeId(empId);
    attendance.setEmployeeName(employee.getFullName());
    attendance.setDate(today);
    attendance.setCheckIn(LocalDateTime.now());
    attendance.setCreatedAt(new java.util.Date());
    attendance.setStatus(LocalTime.now().isAfter(LATE_TIME) ? "LATE" : "PRESENT");

    Attendance saved = repository.save(attendance);
    String title = "Check-in Successful";

    // Trigger Notification (Existing)
    //sendMobileNotification(empId, title, message, "ATTENDANCE");
    notificationService.create(empId, message, "ATTENDANCE");

    // Return the new Response DTO with the message
    return new CheckInResponse(saved, message,title);
}

  public Attendance checkIn(String email, double lat, double lng) {
    // 1. Fetch employee automatically from DB using email
    Employee employee = employeeRepository.findByEmailAddress(email)
            .orElseThrow(() -> new RuntimeException("Employee record not found"));

    String employeeId = String.valueOf(employee.getEmployeeId()); 
    String employeeName = employee.getFullName();
    String workLoc = employee.getWorkLocation(); // Get your existing field
    LocalDate today = LocalDate.now();

    // 2. Prevent duplicate check-in
    if (repository.findByEmployeeIdAndDate(employeeId, today).isPresent()) {
        throw new RuntimeException("Already checked in today");
    }

    // 3. Location Validation (Location Aware)
    // If the workLocation is NOT "Remote" and NOT "WFH", then check the 40m radius
    boolean isRemote = "REMOTE".equalsIgnoreCase(workLoc) || "WFH".equalsIgnoreCase(workLoc);

    if (!isRemote) {
        double distance = calculateDistance(lat, lng, OFFICE_LAT, OFFICE_LNG);
        if (distance > ALLOWED_RADIUS) {
            throw new RuntimeException("You must be within 40 meters of the office for Office-based work.");
        }
    }

    // 4. Create Attendance Record
    Attendance attendance = new Attendance();
    attendance.setEmployeeId(employeeId);
    attendance.setEmployeeName(employeeName);
    attendance.setDate(today);
    attendance.setCheckIn(LocalDateTime.now());
    attendance.setCreatedAt(new java.util.Date()); 

    // Status Logic
    if (LocalTime.now().isAfter(LATE_TIME)) {
        attendance.setStatus("LATE");
    } else {
        attendance.setStatus("PRESENT");
    }
// 🔔 4. TRIGGER NOTIFICATION FOR empId (Mobile Requirement)
       // sendMobileNotification(employeeId, "Check-in", "Successful at " + LocalTime.now(), "ATTENDANCE");
        notificationService.create(employeeId, null, "ATTENDANCE");
    return repository.save(attendance);
    
}

    // ✅ NEW: Automatic Fetch Check-out
    public Attendance checkOut(String email) {
        Employee employee = employeeRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Employee record not found"));

        String employeeId = String.valueOf(employee.getEmployeeId());

        Attendance attendance = repository.findByEmployeeIdAndDate(employeeId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Check-in record not found for today"));

        LocalDateTime now = LocalDateTime.now();
        attendance.setCheckOut(now);

        Duration duration = Duration.between(attendance.getCheckIn(), now);
        double hours = duration.toMinutes() / 60.0;
        attendance.setWorkingHours(Math.round(hours * 100.0) / 100.0);

        if (now.toLocalTime().isBefore(HALF_DAY_TIME)) {
            attendance.setStatus("HALF_DAY");
        }
        // 🔔 TRIGGER NOTIFICATION FOR empId
       // sendMobileNotification(employeeId, "Check-out", "Successful. Hours: " + attendance.getWorkingHours(), "ATTENDANCE");
        notificationService.create(employeeId, "Check-out successful. Total hours: " + attendance.getWorkingHours(), "ATTENDANCE");

        return repository.save(attendance);
    }
  
public void sendMobileNotification(String empId, String title, String message, String type) {
    Notification notification = Notification.builder()
            .employeeId(empId)
            .title(title)
            .message(message)
            .type(type)
            .date(LocalDate.now())
            .read(false)
            .build();

    notificationRepository.save(notification);
}

// ALSO ADD THIS METHOD so the controller can call it for the mobile app
public List<Notification> getNotificationsForEmployee(String employeeId) {
    return notificationRepository.findByEmployeeIdOrderByDateDesc(employeeId);
}

public AttendanceReportDto getMonthlyReport(String employeeId, int year, int month) {

    LocalDate start = LocalDate.of(year, month, 1);
    LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

    List<Attendance> list =
            repository.findByEmployeeIdAndDateBetween(employeeId, start, end);

    AttendanceReportDto dto = new AttendanceReportDto();

    dto.setPresent(list.stream().filter(a -> "PRESENT".equals(a.getStatus())).count());
    dto.setAbsent(list.stream().filter(a -> "ABSENT".equals(a.getStatus())).count());
    dto.setLate(list.stream().filter(a -> "LATE".equals(a.getStatus())).count());
    dto.setHalfDay(list.stream().filter(a -> "HALF_DAY".equals(a.getStatus())).count());

    double totalHours = list.stream()
        .mapToDouble(a -> a.getWorkingHours() != null ? a.getWorkingHours() : 0)
        .sum();

    dto.setTotalHours(totalHours);

    return dto;
}
public Attendance checkInWithQR(String employeeId, String qrToken) {

    LocalDate today = LocalDate.now();

    if (repository.findByEmployeeIdAndDate(employeeId, today).isPresent()) {
        throw new RuntimeException("Already checked in today");
    }

    // TODO: validate QR token

    Attendance attendance = new Attendance();
    attendance.setEmployeeId(employeeId);
    attendance.setDate(LocalDate.now()); // ✅ MUST HAVE
    attendance.setCheckIn(LocalDateTime.now());
    attendance.setStatus("PRESENT");
       // ✅ ADD THIS: For 1-year auto-deletion
    attendance.setCreatedAt(new java.util.Date()); 

    return repository.save(attendance);
}
public List<CalendarDto> getCalendar(String employeeId, int year, int month) {

    LocalDate start = LocalDate.of(year, month, 1);
    LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

    List<Attendance> list =
            repository.findByEmployeeIdAndDateBetween(employeeId, start, end);

    return list.stream().map(a -> {
        CalendarDto dto = new CalendarDto();
        dto.setDate(a.getDate().toString());
        dto.setStatus(a.getStatus() != null ? a.getStatus() : "ABSENT");
        return dto;
    }).collect(Collectors.toList());
}
public List<Attendance> getMonthlyAttendance(String employeeId, int year, int month) {

    LocalDate start = LocalDate.of(year, month, 1);
    LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

    return repository.findByEmployeeIdAndDateBetween(employeeId, start, end);
}
public List<Attendance> getByEmployee(String employeeId) {
    return repository.findByEmployeeId(employeeId);
}
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; 
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

}

