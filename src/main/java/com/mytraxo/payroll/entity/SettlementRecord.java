package com.mytraxo.payroll.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.AllArgsConstructor; // 👈 Add this
import lombok.NoArgsConstructor;


@Document(collection = "settlements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementRecord {
    @Id
    private String id;
    private String employeeId;
    private String employeeName;
    private LocalDate lastWorkingDay;
    
    private Double finalMonthSalary;
    private Double leaveEncashment;
    private Double noticeRecovery;
    private Double netSettlementAmount;
    
    private String status; // PROCESSED, PAID
    private LocalDateTime processedAt;
}