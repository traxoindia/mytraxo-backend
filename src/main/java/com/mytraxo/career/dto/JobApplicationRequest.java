package com.mytraxo.career.dto;

import lombok.Data;

@Data
public class JobApplicationRequest {

    private String jobId;
    private String name;
    private String email;
    private String phone;
}