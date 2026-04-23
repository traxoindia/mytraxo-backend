package com.mytraxo.holiday.repository;

import com.mytraxo.holiday.entity.Holiday;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends MongoRepository<Holiday, String> {
     // This will fetch all holidays and sort them by date (latest/future first)
    //List<Holiday> findAllByOrderByDateAsc(); 
    // If you want the most recent years at the top:
    //List<Holiday> findAllByOrderByDateDesc();
    // Spring Data Mongo automatically translates these into Mongo Queries
    List<Holiday> findByDateBetween(LocalDate start, LocalDate end);
    Optional<Holiday> findByDate(LocalDate date);
   
}