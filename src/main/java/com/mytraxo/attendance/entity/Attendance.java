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
import java.util.Date;

@Data
@Builder
@NoArgsConstructor   // ✅ REQUIRED
@AllArgsConstructor

@Document(collection = "attendance")

public class Attendance {

    @Id
    private String id;
    private LocalDate date;
    private String employeeId;
    private String employeeName;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private Double workingHours;

    private String status; // PRESENT / LATE / HALF_DAY
       // AUTO-DELETE after 1 year (31,536,000 seconds)
    @Indexed(expireAfterSeconds = 31536000)
    private Date createdAt; 
}