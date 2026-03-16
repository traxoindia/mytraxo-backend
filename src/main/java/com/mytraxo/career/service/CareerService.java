package com.mytraxo.career.service;

import com.mytraxo.career.dto.JobApplicationRequest;
import com.mytraxo.career.dto.JobRequest;
import com.mytraxo.career.entity.Job;
import com.mytraxo.career.entity.JobApplication;
import com.mytraxo.career.repo.JobApplicationRepository;
import com.mytraxo.career.repo.JobRepository;

import com.mytraxo.career.enums.ApplicationStage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CareerService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;

    public CareerService(JobRepository jobRepository,
                         JobApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }
public JobApplication getApplicationProfile(String applicationId){
    return applicationRepository.findById(applicationId).orElseThrow();
}
public JobApplication moveToScreening(String id){

    JobApplication application = applicationRepository.findById(id).orElseThrow();

    application.setStage(ApplicationStage.SCREENING);

    return applicationRepository.save(application);
}
public JobApplication updateStage(String id, String stage){

    JobApplication application = applicationRepository.findById(id).orElseThrow();

    application.setStage(ApplicationStage.valueOf(stage));

    return applicationRepository.save(application);
}
public List<JobApplication> getApplicationsByStage(String stage){

    ApplicationStage applicationStage = ApplicationStage.valueOf(stage);

    return applicationRepository.findByStage(applicationStage);
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
 // UPLOAD CV
   public String uploadCv(MultipartFile file) {

    try {

        String fileName = file.getOriginalFilename();

        // Example saving locally
        Path path = Paths.get("uploads/" + fileName);

        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        return "CV uploaded successfully: " + fileName;

    } catch (Exception e) {
        throw new RuntimeException("File upload failed");
    }
}

     // APPLY JOB
    public JobApplication applyJob(JobApplicationRequest request){
        JobApplication application = JobApplication.builder()

                .jobId(request.getJobId())

                // Personal
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .emailAddress(request.getEmailAddress())
                .currentAddress(request.getCurrentAddress())

                // Professional
                .expectedSalary(request.getExpectedSalary())
                .noticePeriod(request.getNoticePeriod())
                .availableStartDate(request.getAvailableStartDate())
                .willingToRelocate(request.getWillingToRelocate())

                // Education
                .highestQualification(request.getHighestQualification())
                .degreeName(request.getDegreeName())
                .universityCollege(request.getUniversityCollege())
                .fieldOfStudy(request.getFieldOfStudy())
                .graduationYear(request.getGraduationYear())
                .percentageGPA(request.getPercentageGPA())

                // Experience
                .totalExperience(request.getTotalExperience())
                .currentCompany(request.getCurrentCompany())
                .currentJobTitle(request.getCurrentJobTitle())
                .previousCompany(request.getPreviousCompany())

                // Skills
                .keySkills(request.getKeySkills())
                .technicalSkills(request.getTechnicalSkills())

                // Resume
                .cvFileUrl(request.getCvFileUrl())

                // Reference
                .referenceName(request.getReferenceName())
                .stage(ApplicationStage.APPLIED) 
                .status("PENDING")
                .appliedAt(Instant.now())

                .build();

        return applicationRepository.save(application);
    }

    // HR VIEW APPLICATIONS
    public List<JobApplication> getApplications(String jobId){
        return applicationRepository.findByJobId(jobId);
    }
}