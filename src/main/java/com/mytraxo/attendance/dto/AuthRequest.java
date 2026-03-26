package com.mytraxo.attendance.dto;

import lombok.Data;
import lombok.Builder;

@Builder
@Data
public class AuthRequest {
    private String email;
    private String phone; // This acts as the password during LOGIN
}