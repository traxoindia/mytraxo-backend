package com.mytraxo.payroll.dto;

import lombok.Data;
import java.time.LocalDate;
import lombok.Builder;
import lombok.AllArgsConstructor; // 👈 Add this
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor  // 👈 Add this line to both files
@AllArgsConstructor

public class SettlementRequest {
    private String employeeId;
    private LocalDate lwd; // Last Working Day
    private int unusedLeaves;
    private boolean noticeServed;
}