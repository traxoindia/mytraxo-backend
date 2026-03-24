package com.mytraxo.attendance.dto;

import lombok.Data;

@Data
public class QRRequest {
    private String employeeId;
    private String qrToken;
}