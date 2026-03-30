package com.mytraxo.attendance.dto;

import lombok.Data;
import java.time.LocalTime;
import lombok.Builder;
import lombok.AllArgsConstructor; // 👈 Add this
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor  // 👈 Add this line to both files
@AllArgsConstructor

public class AttendanceStatusDto {
    private String employeeId;
    private String fullName;
    private String department;
     private LocalTime checkInTime;  // Setter generated: setCheckInTime
    private LocalTime checkOutTime; // Setter generated: setCheckOutTime
    private String status; // PRESENT, ABSENT, LEAVE, etc.
}