package com.mytraxo.career.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "job_applications")
public class JobApplication {

    @Id
    private String id;

    private String jobId;
    private String name;
    private String email;
    private String phone;

    private String resumeUrl;

    private String status;

    private Instant appliedAt;
}