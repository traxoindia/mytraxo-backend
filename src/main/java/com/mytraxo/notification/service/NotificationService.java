package com.mytraxo.notification.service;

import com.mytraxo.notification.entity.Notification;
import com.mytraxo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public void create(String employeeId, String message,String type) {

        Notification n = new Notification();
        n.setEmployeeId(employeeId); 
        n.setMessage(message);
        n.setType(type);
        n.setCreatedAt(LocalDateTime.now());
        repository.save(n);
    }

    public List<Notification> getByUser(String employeeId) {
        return repository.findByEmployeeId(employeeId);
    }
}