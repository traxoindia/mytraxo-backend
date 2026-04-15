package com.mytraxo.holiday.service;

import com.mytraxo.holiday.entity.Holiday;
import com.mytraxo.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public Holiday addHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);
    }

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public List<Holiday> getHolidaysByMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return holidayRepository.findByDateBetween(start, end);
    }

    public boolean isHoliday(LocalDate date) {
        return holidayRepository.findByDate(date).isPresent();
    }
}