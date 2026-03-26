package com.mytraxo.attendance.dto;

import lombok.Data;
import lombok.Builder;

@Builder
@Data
public class CheckInRequest {
 // Used as password
    private double lat;
    private double lng;
}

