package com.mytraxo.payroll.repository;

import com.mytraxo.payroll.entity.PayrollRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends MongoRepository<PayrollRecord, String> {
    Optional<PayrollRecord> findByEmployeeIdAndMonthAndYear(String employeeId, int month, int year);
       // Add this to get the employee's history
    List<PayrollRecord> findByEmployeeIdOrderByYearDescMonthDesc(String employeeId);
}