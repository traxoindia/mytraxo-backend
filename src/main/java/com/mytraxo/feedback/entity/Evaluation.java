
package com.mytraxo.feedback.entity;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "evalution")
public class Evaluation {

    private Rating communicationSkills;
    private Rating technicalKnowledge;
    private Rating problemSolving;
    private Rating relevantExperience;
    private Rating culturalFit;
}