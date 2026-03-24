package com.mytraxo.attendance.dto;

import lombok.Data;

@Data
public class AttendanceReportDto {

    private long present;
    private long absent;
    private long late;
    private long halfDay;

    private double totalHours;
}