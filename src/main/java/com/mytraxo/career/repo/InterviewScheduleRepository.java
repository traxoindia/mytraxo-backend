package com.mytraxo.career.repo;

import com.mytraxo.career.entity.InterviewSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterviewScheduleRepository extends MongoRepository<InterviewSchedule, String> {

    List<InterviewSchedule> findByApplicationId(String applicationId);
}