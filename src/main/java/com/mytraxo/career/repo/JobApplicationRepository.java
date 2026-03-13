package com.mytraxo.career.repo;

import com.mytraxo.career.entity.JobApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobApplicationRepository extends MongoRepository<JobApplication,String> {

    List<JobApplication> findByJobId(String jobId);

}