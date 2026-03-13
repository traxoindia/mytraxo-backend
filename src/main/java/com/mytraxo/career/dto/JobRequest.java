package com.mytraxo.career.dto;

import lombok.Data;

@Data
public class JobRequest {

    private String jobTitle;
    private String position;
    private String department;
    private String description;
    private String location;
}