package com.mytraxo.feedback.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mytraxo.feedback.entity.Feedback;


public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    List<Feedback> findByApplicationId(String applicationId);
}
