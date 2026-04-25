package com.mytraxo.attendance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.index.Indexed;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@NoArgsConstructor   // ✅ REQUIRED
@AllArgsConstructor

@Document(collection = "notifications") // SEPARATE collection = NO impact on web
public class Notification {
      @Id
    private String id;
    private String employeeId;
    private String title;      // e.g., "Check-in Successful", "Leave Approved"
    private String message;    // e.g., "You checked in at 09:00 AM"
    private String type;       // e.g., "ATTENDANCE", "LEAVE", "PAYROLL"
    private LocalDate date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkInTime; // If you use this for mobile
    
    @Builder.Default 
    private boolean read = false;
    // ✅ ADD THIS FOR AUTO-DELETE AFTER 1 YEAR
    @Indexed(expireAfterSeconds = 31536000)
    private java.util.Date createdAt; 
}
