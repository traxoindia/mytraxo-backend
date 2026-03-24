package com.mytraxo.attendance.dto;

import lombok.Data;

@Data
public class CheckInRequest {
    private String employeeId;
    private String employeeName;
    private double lat;
    private double lng;
}