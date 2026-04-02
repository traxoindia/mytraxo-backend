package com.mytraxo.employee.dto;

import lombok.Data;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@NoArgsConstructor  // Add this
@AllArgsConstructor // Add this
@Builder
@Data
public class LeaveEmployeeRequest {
    private String leavingDate;
    private String leavingReason;
}