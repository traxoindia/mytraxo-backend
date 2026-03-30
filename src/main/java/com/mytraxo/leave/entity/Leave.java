package com.mytraxo.leave.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "leave")
public class Leave {

    @Id
    private String id;

    private String employeeId;
    private String type; // FULL_DAY / HALF_DAY
    private LocalDate fromDate; 
    private LocalDate toDate;
    private String reason;

    private String status; // PENDING / APPROVED / REJECTED
}