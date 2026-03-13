package com.mytraxo.career.service;

import com.mytraxo.career.dto.JobApplicationRequest;
import com.mytraxo.career.dto.JobRequest;
import com.mytraxo.career.entity.Job;
import com.mytraxo.career.entity.JobApplication;
import com.mytraxo.career.repo.JobApplicationRepository;
import com.mytraxo.career.repo.JobRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CareerService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;

    public CareerService(JobRepository jobRepository,
                         JobApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    // HR POST JOB
    public Job createJob(JobRequest request){

        Job job = Job.builder()
                .jobTitle(request.getJobTitle())
                .position(request.getPosition())
                .department(request.getDepartment())
                .description(request.getDescription())
                .location(request.getLocation())
                .active(true)
                .createdAt(Instant.now())
                .build();

        return jobRepository.save(job);
    }

    // PUBLIC JOB LIST
    public List<Job> getJobs(){
        return jobRepository.findByActiveTrue();
    }

    // APPLY JOB
    public JobApplication applyJob(JobApplicationRequest request){

        JobApplication application = JobApplication.builder()
                .jobId(request.getJobId())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status("APPLIED")
                .appliedAt(Instant.now())
                .build();

        return applicationRepository.save(application);
    }

    // HR VIEW APPLICATIONS
    public List<JobApplication> getApplications(String jobId){
        return applicationRepository.findByJobId(jobId);
    }
}