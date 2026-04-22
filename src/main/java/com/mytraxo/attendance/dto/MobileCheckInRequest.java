package com.mytraxo.attendance.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileCheckInRequest {
    private String employeeId;
    //private String employeeName;
    private double lat;
    private double lng;
}