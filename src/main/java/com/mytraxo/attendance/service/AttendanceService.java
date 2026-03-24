package com.mytraxo.attendance.service;

import com.mytraxo.attendance.entity.Attendance;
import com.mytraxo.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import com.mytraxo.attendance.dto.AttendanceReportDto;
import com.mytraxo.attendance.dto.CalendarDto;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository repository;

    // ⏰ Late after 10:15 AM
    private static final LocalTime LATE_TIME = LocalTime.of(10, 15);
    private static final LocalTime HALF_DAY_TIME = LocalTime.of(17, 0);
    private static final double OFFICE_LAT = 22.5726;   // example (update with your office)
    private static final double OFFICE_LNG = 88.3639;
    private static final double ALLOWED_RADIUS = 40; // meters

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

    final int R = 6371000; // Earth radius in meters

    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
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

    // ✅ CHECK-IN
    LocalDate today = LocalDate.now();
   public Attendance checkIn(String employeeId, String employeeName,
                          double lat, double lng) {


    if (repository.findByEmployeeIdAndDate(employeeId, today).isPresent()) {
        throw new RuntimeException("Already checked in today");
    }

    // 📍 LOCATION VALIDATION
    double distance = calculateDistance(lat, lng, OFFICE_LAT, OFFICE_LNG);

    if (distance > ALLOWED_RADIUS) {
        throw new RuntimeException("You must be within 40 meters of office to check-in");
    }

    Attendance attendance = new Attendance();
    attendance.setEmployeeId(employeeId);
    attendance.setEmployeeName(employeeName);
    attendance.setDate(today);

    LocalDateTime now = LocalDateTime.now();
    attendance.setCheckIn(now);

    // Late after 10:15
  if (now.toLocalTime().isBefore(HALF_DAY_TIME)) {
    attendance.setStatus("HALF_DAY");
} else if ("LATE".equals(attendance.getStatus())) {
    attendance.setStatus("LATE_PRESENT");
} else {
    attendance.setStatus("PRESENT");
}

    return repository.save(attendance);
}
public List<Attendance> getMonthlyAttendance(String employeeId, int year, int month) {

    LocalDate start = LocalDate.of(year, month, 1);
    LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

    return repository.findByEmployeeIdAndDateBetween(employeeId, start, end);
}
public List<Attendance> getByEmployee(String employeeId) {
    return repository.findByEmployeeId(employeeId);
}
    // ✅ CHECK-OUT
    public Attendance checkOut(String employeeId) {

        

        Attendance attendance = repository.findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> new RuntimeException("Check-in not found"));

       if (attendance.getCheckIn() == null) {
    throw new RuntimeException("Invalid check-in time");
}
        LocalDateTime now = LocalDateTime.now();
        attendance.setCheckOut(now);

        // 🕒 Calculate working hours
        Duration duration = Duration.between(attendance.getCheckIn(), now);
        double hours = duration.toMinutes() / 60.0;
        attendance.setWorkingHours(hours);

        // 🔥 Half-day logic
        if (now.toLocalTime().isBefore(HALF_DAY_TIME)) {
    attendance.setStatus("HALF_DAY");
} else if (!"LATE".equals(attendance.getStatus())) {
    attendance.setStatus("PRESENT");
}

        return repository.save(attendance);
    }
}