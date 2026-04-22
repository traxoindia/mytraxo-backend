package com.mytraxo.attendance.dto;

import com.mytraxo.attendance.entity.Attendance;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor  // Required for JSON parsing
@AllArgsConstructor
public class CheckInResponse {
    private Attendance attendance;
    private String message;
}
