package com.mytraxo.career.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class Job {

    @Id
    private String id;

    private String jobTitle;
    private String position;
    private String department;
    private String description;
    private String location;

    private boolean active;

    private Instant createdAt;
}