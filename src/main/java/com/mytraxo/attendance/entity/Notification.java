package com.mytraxo.attendance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor   // ✅ REQUIRED
@AllArgsConstructor

@Document(collection = "notifications") // SEPARATE collection = NO impact on web
public class Notification {
    @Id
    private String id;
    
    private String employeeId;
    
    private String message;
     private java.time.LocalDate date;
     @Builder.Default // ✅ ADD THIS LINE to fix the warning
    private boolean isRead = false;
}