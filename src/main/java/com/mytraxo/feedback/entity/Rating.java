package com.mytraxo.feedback.entity;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "rating")
public class Rating {

    private int rating;
    private String comments;
}