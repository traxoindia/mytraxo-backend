package com.mytraxo.career.service;

import com.mytraxo.career.dto.JobApplicationRequest;
import com.mytraxo.career.dto.JobRequest;
import com.mytraxo.career.entity.Job;
import com.mytraxo.career.entity.JobApplication;
import com.mytraxo.career.repo.JobApplicationRepository;
import com.mytraxo.career.repo.JobRepository;
import com.mytraxo.career.dto.InterviewRequest;
import com.mytraxo.career.entity.InterviewSchedule;
import com.mytraxo.career.repo.InterviewScheduleRepository;
import com.mytraxo.career.enums.ApplicationStage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mytraxo.bgv.service.BGVService; // Correctly imported

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
    private final JobApplicationRepository applicationRepository; // We will use this name
    private final InterviewScheduleRepository interviewRepository;
    private final BGVService bgvService;

    // FIXED CONSTRUCTOR: Added BGVService here
    public CareerService(JobRepository jobRepository,
                         JobApplicationRepository applicationRepository,
                         InterviewScheduleRepository interviewRepository,
                         BGVService bgvService) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.bgvService = bgvService;
    }

    public JobApplication getApplicationProfile(String applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow();
    }

    public JobApplication moveToScreening(String id) {
        JobApplication application = applicationRepository.findById(id).orElseThrow();
        application.setStage(ApplicationStage.SCREENING);
        return applicationRepository.save(application);
    }

    // UPDATED: This now triggers BGV if stage is SELECTED
    public JobApplication updateStage(String id, String stage) {
        JobApplication application = applicationRepository.findById(id).orElseThrow();
        
        ApplicationStage newStage = ApplicationStage.valueOf(stage);
        application.setStage(newStage);
        
        JobApplication savedApp = applicationRepository.save(application);

        // TRIGGER AUTOMATIC BGV
        if (newStage == ApplicationStage.SELECTED) {
            bgvService.initiateBGVProcess(id);
        }

        return savedApp;
    }

    public InterviewSchedule scheduleInterview(InterviewRequest request) {
        JobApplication application = applicationRepository
                .findById(request.getApplicationId())
                .orElseThrow();

        application.setStage(request.getInterviewRound());
        applicationRepository.save(application);

        InterviewSchedule interview = InterviewSchedule.builder()
                .applicationId(request.getApplicationId())
                .interviewDate(request.getInterviewDate())
                .interviewTime(request.getInterviewTime())
                .interviewRound(request.getInterviewRound())
                .interviewerName(request.getInterviewerName())
                .interviewLink(request.getInterviewLink())
                .notes(request.getNotes())
                .status("SCHEDULED")
                .build();

        return interviewRepository.save(interview);
    }

    public List<JobApplication> getApplicationsByStage(String stage) {
        ApplicationStage applicationStage = ApplicationStage.valueOf(stage);
        return applicationRepository.findByStage(applicationStage);
    }

    public Job createJob(JobRequest request) {
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

    public List<Job> getJobs() {
        return jobRepository.findByActiveTrue();
    }

    public String uploadCv(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return "CV uploaded successfully: " + fileName;
        } catch (Exception e) {
            throw new RuntimeException("File upload failed");
        }
    }

    public JobApplication applyJob(JobApplicationRequest request) {
        JobApplication application = JobApplication.builder()
                .jobId(request.getJobId())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .emailAddress(request.getEmailAddress())
                .currentAddress(request.getCurrentAddress())
                .expectedSalary(request.getExpectedSalary())
                .noticePeriod(request.getNoticePeriod())
                .availableStartDate(request.getAvailableStartDate())
                .willingToRelocate(request.getWillingToRelocate())
                .highestQualification(request.getHighestQualification())
                .degreeName(request.getDegreeName())
                .universityCollege(request.getUniversityCollege())
                .fieldOfStudy(request.getFieldOfStudy())
                .graduationYear(request.getGraduationYear())
                .percentageGPA(request.getPercentageGPA())
                .totalExperience(request.getTotalExperience())
                .currentCompany(request.getCurrentCompany())
                .currentJobTitle(request.getCurrentJobTitle())
                .previousCompany(request.getPreviousCompany())
                .keySkills(request.getKeySkills())
                .technicalSkills(request.getTechnicalSkills())
                .cvFileUrl(request.getCvFileUrl())
                .referenceName(request.getReferenceName())
                .stage(ApplicationStage.APPLIED)
                .status("PENDING")
                .appliedAt(Instant.now())
                .build();

        return applicationRepository.save(application);
    }

    // FIXED: Corrected Repository name to 'applicationRepository'
    public void updateApplicationStage(String id, ApplicationStage stage) {
        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStage(stage);
        applicationRepository.save(application);

        if (stage == ApplicationStage.SELECTED) {
            bgvService.initiateBGVProcess(id);
        }
    }

    public List<JobApplication> getApplications(String jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<InterviewSchedule> getInterviews(String applicationId) {
        return interviewRepository.findByApplicationId(applicationId);
    }

    public List<JobApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<JobApplication> getSelectedCandidates() {
        return applicationRepository.findByStage(ApplicationStage.HIRED);
    }
}