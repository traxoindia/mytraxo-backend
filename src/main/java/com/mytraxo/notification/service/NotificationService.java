package com.mytraxo.notification.service;

import com.mytraxo.notification.entity.Notification;
import com.mytraxo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public void create(String employeeId, String originalMessage,String type) {

        Notification n = new Notification();
        n.setEmployeeId(employeeId); 
       // 1. Smart Message (No nanoseconds)
        n.setMessage(generateSmartMessage());
        n.setType(type);
      // 1. Fixed Message Logic: Only use smart greeting for Attendance
        if ("ATTENDANCE".equalsIgnoreCase(type)) {
            n.setMessage(generateSmartMessage());
        } else {
            n.setMessage(originalMessage); // Uses "Your leave has been approved" etc.
        }

        // 2. Fixed Date Logic: Force IST (Asia/Kolkata)
        ZoneId istZone = ZoneId.of("Asia/Kolkata");
        n.setDate(LocalDate.now(istZone).toString()); 
        n.setCreatedAt(LocalDateTime.now(istZone));

        repository.save(n);
    }

    private String generateSmartMessage() {
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        
        int hour = now.getHour();
        String greeting = (hour < 12) ? "Good Morning" : (hour < 16) ? "Good Afternoon" : "Good Evening";
        
        return String.format("%s! Check-in successful at %s", greeting, now.format(timeFormatter));
    }
    

    public List<Notification> getByUser(String employeeId) {
        return repository.findByEmployeeId(employeeId);
    }
}