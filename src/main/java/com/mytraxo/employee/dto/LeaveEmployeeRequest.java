package com.mytraxo.employee.dto;

import lombok.Data;

@Data
public class LeaveEmployeeRequest {
    private String leavingDate;
    private String leavingReason;
}