package com.mytraxo.holiday.entity;

import lombok.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Document(collection = "holidays") // MongoDB Collection name
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Holiday {
    @Id
    private String id; // MongoDB uses String IDs by default

    private LocalDate date;
    private String name;
    private String description;
    private String type; // "PUBLIC", "OPTIONAL"
}