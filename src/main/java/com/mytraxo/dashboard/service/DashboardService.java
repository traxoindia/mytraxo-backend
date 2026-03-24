package com.mytraxo.dashboard.service;

import com.mytraxo.attendance.entity.Attendance;
import com.mytraxo.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AttendanceRepository attendanceRepository;

    public Map<String, Long> getStats() {

    LocalDate today = LocalDate.now();
    List<Attendance> list = attendanceRepository.findAll();

    // ✅ Present Today
    long presentToday = list.stream()
            .filter(a -> today.equals(a.getDate()))
            .count();
    // ✅ Late
    long late = list.stream()
            .filter(a -> today.equals(a.getDate()) && "LATE".equalsIgnoreCase(a.getStatus()))
            .count();

    // ✅ Half Day
    long halfDay = list.stream()
            .filter(a -> today.equals(a.getDate()) && "HALF_DAY".equalsIgnoreCase(a.getStatus()))
            .count();

    // ✅ Total Employees (unique employeeId)
    long totalEmployees = list.stream()
            .map(Attendance::getEmployeeId)
            .distinct()
            .count();

    // ✅ Absent Today
    long absentToday = totalEmployees - presentToday;

    Map<String, Long> map = new HashMap<>();
    map.put("totalEmployees", totalEmployees);
    map.put("presentToday", presentToday);
    map.put("absentToday", absentToday);
    map.put("late", late);
    map.put("halfDay", halfDay);

    return map;
}
}