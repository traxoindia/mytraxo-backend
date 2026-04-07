package com.mytraxo.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor; // 👈 Add this
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor  // 👈 Add this line to both files
@AllArgsConstructor
@Data
public class ReportRequest {
    private String employeeId;
    private String employeeName;
    private Integer day;   // e.g., 25
    private Integer month; // e.g., 10
    private Integer year;  // e.g., 2023
}