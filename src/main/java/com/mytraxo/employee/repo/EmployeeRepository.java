package com.mytraxo.employee.repo;

import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.entity.EmployeeStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByEmployeeId(String employeeId);
    List<Employee> findByEmploymentStatus(EmployeeStatus status);
    boolean existsByEmployeeId(String employeeId);
    Optional<Employee> findByEmailAddress(String emailAddress);
}