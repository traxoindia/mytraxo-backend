package com.mytraxo.payroll.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.AllArgsConstructor; // 👈 Add this
import lombok.NoArgsConstructor;


@Document(collection = "payrolls")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRecord {
    @Id
    private String id;
    private String employeeId;
    private String employeeName;
    private int month;
    private int year;

    private Integer totalDaysInMonth;
    private Long presentDays;

    private Double basicPay;
    private Double hra;
    private Double grossEarned;
    
    private Double pfDeduction;
    private Double taxDeduction;
    private Double netSalary;

    private String paymentStatus; // PENDING, PAID
    private LocalDateTime generatedAt;
}