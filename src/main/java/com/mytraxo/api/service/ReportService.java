package com.mytraxo.api.service;

import com.mytraxo.api.dto.ReportRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MongoTemplate mongoTemplate;

    public <T> List<T> getWiseReport(ReportRequest request, Class<T> entityClass, String dateField) {
        Query query = new Query();

        // 1. Filter by Employee ID
        if (request.getEmployeeId() != null && !request.getEmployeeId().isEmpty()) {
            query.addCriteria(Criteria.where("employeeId").is(request.getEmployeeId()));
        }
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
    query.addCriteria(Criteria.where("status").is(request.getStatus()));
}

        // 2. Date Filtering using Range (Fix for ISODate objects)
        if (request.getYear() != null) {
            LocalDateTime start;
            LocalDateTime end;

            if (request.getMonth() != null && request.getDay() != null) {
                // Specific Day: From 00:00:00 to 23:59:59
                LocalDate targetDate = LocalDate.of(request.getYear(), request.getMonth(), request.getDay());
                start = targetDate.atStartOfDay();
                end = targetDate.atTime(LocalTime.MAX);
            } 
            else if (request.getMonth() != null) {
                // Specific Month: From 1st day to last day
                LocalDate firstDay = LocalDate.of(request.getYear(), request.getMonth(), 1);
                start = firstDay.atStartOfDay();
                end = firstDay.plusMonths(1).atStartOfDay();
            } 
            else {
                // Specific Year: From Jan 1st to Dec 31st
                LocalDate firstDayOfYear = LocalDate.of(request.getYear(), 1, 1);
                start = firstDayOfYear.atStartOfDay();
                end = firstDayOfYear.plusYears(1).atStartOfDay();
            }

            // This matches MongoDB ISODate objects correctly
            query.addCriteria(Criteria.where(dateField).gte(start).lte(end));
        }

        return mongoTemplate.find(query, entityClass);
    }
}