package com.mytraxo.leave.repository;

import com.mytraxo.leave.entity.Leave;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeaveRepository extends MongoRepository<Leave, String> {

    List<Leave> findAll(); 
    List<Leave> findByEmployeeId(String employeeId);
    List<Leave> findByStatus(String status);
}