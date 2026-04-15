package com.mytraxo.holiday.controller;

import com.mytraxo.holiday.entity.Holiday;
import com.mytraxo.holiday.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    @PostMapping("/admin/add")
    public ResponseEntity<Holiday> addHoliday(@RequestBody Holiday holiday) {
        return ResponseEntity.ok(holidayService.addHoliday(holiday));
    }

    @GetMapping
    public ResponseEntity<List<Holiday>> getAll() {
        return ResponseEntity.ok(holidayService.getAllHolidays());
    }
}