package com.mytraxo.leave.service;

import com.mytraxo.leave.entity.Leave;
import com.mytraxo.leave.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mytraxo.attendance.entity.Attendance;
import com.mytraxo.attendance.repository.AttendanceRepository;
import com.mytraxo.notification.entity.Notification;
import com.mytraxo.notification.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class LeaveService {

    private final LeaveRepository repository;
    private final AttendanceRepository attendanceRepository;
    private final NotificationRepository notificationRepository;
    
    // ✅ Apply Leave
    public Leave applyLeave(Leave leave) {
        leave.setStatus("PENDING");
        return repository.save(leave);
    }

    // ✅ Approve Leave
    public Leave approve(String id) {

    Leave leave = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Leave not found"));

    leave.setStatus("APPROVED");
    
    Notification notification = new Notification();
notification.setEmployeeId(leave.getEmployeeId());
notification.setMessage("Your leave has been approved");
notification.setType("LEAVE");
notification.setCreatedAt(LocalDateTime.now());

notificationRepository.save(notification);

    // 🔥 CREATE ATTENDANCE ENTRY
    Attendance attendance = new Attendance();
    attendance.setEmployeeId(leave.getEmployeeId());
    attendance.setDate(leave.getDate()); // ✅ use correct field

    if ("HALF_DAY".equalsIgnoreCase(leave.getType())) {
        attendance.setStatus("HALF_DAY");
    } else {
        attendance.setStatus("LEAVE");
    }

    attendanceRepository.save(attendance);

    return repository.save(leave);
}

    // ✅ Reject Leave
    public Leave reject(String id) {
        Leave leave = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus("REJECTED");
        return repository.save(leave);
    }

    public List<Leave> getByEmployee(String employeeId) {
        return repository.findByEmployeeId(employeeId);
    }
}