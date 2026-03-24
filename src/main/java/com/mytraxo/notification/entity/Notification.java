package com.mytraxo.notification.entity;

import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String employeeId;
    private String message;
    private String type;
    private LocalDateTime createdAt;
}