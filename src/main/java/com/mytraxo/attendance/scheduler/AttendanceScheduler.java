package com.mytraxo.attendance.scheduler;

import com.mytraxo.attendance.entity.Attendance;
import com.mytraxo.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AttendanceScheduler {

    private final AttendanceRepository repository;

    // ⏰ Runs every day at 11:59 PM
    @Scheduled(cron = "0 59 23 * * ?")
    public void markAbsent() {

        LocalDate today = LocalDate.now();

        // ⚠️ You should fetch all employees (simplified here)
        List<String> allEmployees = List.of("emp1", "emp2"); // replace with DB

        for (String empId : allEmployees) {

            boolean exists = repository
                    .findByEmployeeIdAndDate(empId, today)
                    .isPresent();

            if (!exists) {
                Attendance att = new Attendance();
                att.setEmployeeId(empId);
                att.setDate(today);
                att.setStatus("ABSENT");

                repository.save(att);
            }
        }
    }
}