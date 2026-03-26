package com.mytraxo.attendance.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor; // 👈 Add this
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor  // 👈 Add this line to both files
@AllArgsConstructor
public class CalendarDto {

    private String date;
    private String status;
}