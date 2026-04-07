package com.mytraxo.api.service;

import com.mytraxo.api.dto.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

   private final MongoTemplate mongoTemplate;

     public <T> List<T> getWiseReport(ReportRequest request, Class<T> entityClass, String dateField) {
        Query query = new Query();

        // 1. Filter by Employee ID or Name
        if (request.getEmployeeId() != null && !request.getEmployeeId().isEmpty()) {
            query.addCriteria(Criteria.where("employeeId").is(request.getEmployeeId()));
        } else if (request.getEmployeeName() != null && !request.getEmployeeName().isEmpty()) {
            // Case-insensitive regex search allows finding "John" in "John Doe"
            query.addCriteria(Criteria.where("employeeName").regex(request.getEmployeeName(), "i"));
        }

        // 2. String-Based Date Filtering (Works with your String dateOfJoining)
        if (request.getYear() != null) {
            String datePattern = "";
            
            if (request.getMonth() != null) {
                String monthStr = String.format("%02d", request.getMonth());
                if (request.getDay() != null) {
                    String dayStr = String.format("%02d", request.getDay());
                    // Matches YYYY-MM-DD
                    datePattern = request.getYear() + "-" + monthStr + "-" + dayStr;
                } else {
                    // Matches YYYY-MM
                    datePattern = request.getYear() + "-" + monthStr;
                }
            } else {
                // Matches YYYY
                datePattern = String.valueOf(request.getYear());
            }

            // Using Regex "contains" logic so it works on Strings
            query.addCriteria(Criteria.where(dateField).regex(datePattern));
        }

        return mongoTemplate.find(query, entityClass);
    }
}