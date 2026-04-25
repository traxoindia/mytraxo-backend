package com.mytraxo.notification.entity;

import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String employeeId;
    private String message;
    private String type;
    private String date; 
  // Add this to keep consistency across your whole app
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}