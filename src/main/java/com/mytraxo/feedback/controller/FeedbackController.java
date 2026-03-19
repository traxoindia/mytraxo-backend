package com.mytraxo.feedback.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mytraxo.feedback.entity.Feedback;
import com.mytraxo.feedback.service.FeedbackService;

import com.mytraxo.feedback.dto.FeedbackRequest;

@RestController
@RequestMapping("/api/careers/feedback")
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

   @PostMapping
public ResponseEntity<Feedback> createFeedback(@RequestBody FeedbackRequest request) {
    return ResponseEntity.ok(service.saveFeedback(request));
}
    @GetMapping("/application/{id}")
public ResponseEntity<List<Feedback>> getByApplication(@PathVariable String id) {
    return ResponseEntity.ok(service.getByApplicationId(id));
}
@GetMapping
public List<Feedback> getAll() {
    return service.getAll();
}
}