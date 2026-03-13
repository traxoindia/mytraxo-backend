package com.mytraxo.career.controller;

import com.mytraxo.career.dto.JobApplicationRequest;
import com.mytraxo.career.dto.JobRequest;
import com.mytraxo.career.entity.Job;
import com.mytraxo.career.entity.JobApplication;
import com.mytraxo.career.service.CareerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
public class CareerController {

    private final CareerService service;

    public CareerController(CareerService service) {
        this.service = service;
    }

    // HR POST JOB
    @PostMapping("/jobs")
    public Job createJob(@RequestBody JobRequest request){
        return service.createJob(request);
    }

    // PUBLIC JOB LIST
    @GetMapping("/jobs")
    public List<Job> getJobs(){
        return service.getJobs();
    }

    // APPLY JOB
    @PostMapping("/apply")
    public JobApplication applyJob(@RequestBody JobApplicationRequest request){
        return service.applyJob(request);
    }

    // HR VIEW APPLICATIONS
    @GetMapping("/applications/{jobId}")
    public List<JobApplication> getApplications(@PathVariable String jobId){
        return service.getApplications(jobId);
    }
}