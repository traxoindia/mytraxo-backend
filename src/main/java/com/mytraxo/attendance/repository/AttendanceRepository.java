package com.mytraxo.attendance.repository;

import com.mytraxo.attendance.entity.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    Optional<Attendance> findByEmployeeIdAndDate(String employeeId, LocalDate date);

    List<Attendance> findByEmployeeId(String employeeId);
    List<Attendance> findByEmployeeIdAndDateBetween(
    String employeeId,
    LocalDate start,
    LocalDate end
);
}
