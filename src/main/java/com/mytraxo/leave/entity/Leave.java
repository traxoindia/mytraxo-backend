package com.mytraxo.leave.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leave")
public class Leave {

    @Id
    private String id;

    private String employeeId;
    private String type; // FULL_DAY / HALF_DAY
    // NEW FIELDS FOR MOBILE (Will not affect web)
    private String leaveCategory; // Casual, Sick, Medical, Other
    private String applyFor;      // Single Day / Multiple Days
    private String documentUrl;   // URL for the uploaded file
    private LocalDate fromDate; 
    private LocalDate toDate;
    private String reason;

    private String status; // PENDING / APPROVED / REJECTED
       @Indexed(expireAfterSeconds = 31536000)
    private Date createdAt;
}
