package com.mytraxo.career.repo;

import com.mytraxo.career.entity.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobRepository extends MongoRepository<Job,String> {

    List<Job> findByActiveTrue();

}