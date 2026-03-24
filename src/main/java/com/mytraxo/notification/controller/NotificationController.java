package com.mytraxo.notification.controller;

import com.mytraxo.notification.entity.Notification;
import com.mytraxo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public List<Notification> getNotifications(@RequestParam String employeeId) {
        return service.getByUser(employeeId);
    }
}