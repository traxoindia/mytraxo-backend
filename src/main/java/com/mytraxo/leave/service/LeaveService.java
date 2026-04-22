package com.mytraxo.leave.service;

import com.mytraxo.leave.entity.Leave;
import com.mytraxo.leave.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import com.mytraxo.attendance.service.AttendanceService;

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
    private final AttendanceService attendanceService; 
    
    // ✅ Apply Leave
    public Leave applyLeave(Leave leave) {
        leave.setStatus("PENDING");
           // ✅ ADD THIS: Set createdAt so the leave record deletes after 1 year
    leave.setCreatedAt(new java.util.Date()); 
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
     // 2. MOBILE Notification (Call the Service, not the entity)
        // Note: Used getFromDate() because your entity seems to use that field
        attendanceService.sendMobileNotification(
             leave.getEmployeeId(),
            "Leave Approved", 
           "Your leave for " + leave.getFromDate() + " is approved", 
            "LEAVE"
        );

        // 3. CREATE ATTENDANCE ENTRY
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(leave.getEmployeeId());
        attendance.setDate(leave.getFromDate()); 
        attendance.setCreatedAt(new java.util.Date()); 

        if ("HALF_DAY".equalsIgnoreCase(leave.getType())) {
            attendance.setStatus("HALF_DAY");
        } else {
            attendance.setStatus("LEAVE");
        }

        attendanceRepository.save(attendance);
        return repository.save(leave);
    }
public Leave applyLeaveMobile(Leave leave, String filePath) {
    leave.setStatus("PENDING");
    leave.setCreatedAt(new java.util.Date()); 
    leave.setDocumentUrl(filePath); // Save the file path if provided
    
    Leave savedLeave = repository.save(leave);
    
    // Trigger notification logic for the mobile app requirement
    Notification notification = new Notification();
    notification.setEmployeeId(leave.getEmployeeId());
    notification.setMessage("New " + leave.getLeaveCategory() + " leave request submitted.");
    notification.setType("LEAVE_REQUEST");
    notification.setCreatedAt(LocalDateTime.now());
    notificationRepository.save(notification);
    
    return savedLeave;
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
    public List<Leave> getAllLeaves() {
    return repository.findAll();
}

public List<Leave> getPendingLeaves() {
    return repository.findByStatus("PENDING");
}

public List<Leave> getLeavesByEmployee(String employeeId) {
    return repository.findByEmployeeId(employeeId);
}
}