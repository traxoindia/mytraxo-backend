package com.mytraxo.career.controller;

import com.mytraxo.career.dto.JobApplicationRequest;
import com.mytraxo.career.dto.JobRequest;
import com.mytraxo.career.entity.Job;
import com.mytraxo.career.entity.JobApplication;
import com.mytraxo.career.service.CareerService;

import com.mytraxo.career.dto.InterviewRequest;
import com.mytraxo.career.entity.InterviewSchedule;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    //View Candidate Profile API
    @GetMapping("/applications/profile/{applicationId}")
public JobApplication getApplicationProfile(@PathVariable String applicationId){
    return service.getApplicationProfile(applicationId);
}
//Move Candidate to Screening
@PutMapping("/applications/{id}/screening")
public JobApplication moveToScreening(@PathVariable String id){
    return service.moveToScreening(id);
}
@PostMapping("/interview/schedule")
public InterviewSchedule schedule(@RequestBody InterviewRequest request){
    return service.scheduleInterview(request);
}
@GetMapping("/interview/{applicationId}")
public List<InterviewSchedule> getInterviews(@PathVariable String applicationId){
    return service.getInterviews(applicationId);
}
//Move Candidate to Next Round
@PutMapping("/applications/{id}/stage")
public JobApplication updateStage(@PathVariable String id,
                                  @RequestParam String stage){

    return service.updateStage(id, stage);
}
//Recruitment Dashboard API
@GetMapping("/applications/stage/{stage}")
public List<JobApplication> getByStage(@PathVariable String stage){
    return service.getApplicationsByStage(stage);
}
    // PUBLIC JOB LIST
    @GetMapping("/jobs")
    public List<Job> getJobs(){
        return service.getJobs();
    }

    // UPLOAD CV
    @PostMapping("/upload-cv")
    public String uploadCv(@RequestParam("file") MultipartFile file){
        return service.uploadCv(file);
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