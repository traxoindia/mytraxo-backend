package com.mytraxo.attendance.repository;

import com.mytraxo.attendance.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import org.springframework.stereotype.Repository; // <-- Add this import

@Repository("attendanceNotificationRepository") // Give it a unique name

public interface NotificationRepository extends MongoRepository<Notification, String> {
     List<Notification> findByEmployeeIdOrderByDateDesc(String employeeId);
}