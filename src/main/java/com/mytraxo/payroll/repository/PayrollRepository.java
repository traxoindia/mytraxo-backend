package com.mytraxo.payroll.repository;

import com.mytraxo.payroll.entity.PayrollRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends MongoRepository<PayrollRecord, String> {
    Optional<PayrollRecord> findByEmployeeIdAndMonthAndYear(String employeeId, int month, int year);
       // Add this to get the employee's history
    List<PayrollRecord> findByEmployeeIdOrderByYearDescMonthDesc(String employeeId);
     // 3. ADD THIS: Used by HR to see ALL employees for a specific month
    List<PayrollRecord> findByMonthAndYear(int month, int year);

    // 4. ADD THIS: To check if payroll already exists before generating (prevents duplicates)
    boolean existsByEmployeeIdAndMonthAndYear(String employeeId, int month, int year);
}