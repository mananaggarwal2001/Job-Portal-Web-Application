package com.mananluvtocode.jobportal.services;

import com.mananluvtocode.jobportal.entity.*;
import com.mananluvtocode.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobPostActivityService {
    private final JobPostActivityRepository jobPostActivityRepository;

    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobsDTO> recruiterJobsDTOList(int recruiterId) {
        List<IRecruiterJobs> recruiterJobs = jobPostActivityRepository.getRecruiterJob(recruiterId);
        List<RecruiterJobsDTO> recruiterJobsDTOList = new ArrayList<>();
        for (IRecruiterJobs recruiterJobs1 : recruiterJobs) {
            JobLocation location = new JobLocation(recruiterJobs1.getLocationId(), recruiterJobs1.getCity(), recruiterJobs1.getCountry(), recruiterJobs1.getState());
            JobCompany company = new JobCompany(recruiterJobs1.getCompanyId(), "", recruiterJobs1.getName());
            recruiterJobsDTOList.add(new RecruiterJobsDTO(recruiterJobs1.getTotalCandidates(), recruiterJobs1.getJob_post_id(), recruiterJobs1.getJob_title(), location, company));
        }
        return recruiterJobsDTOList;
    }

    public JobPostActivity getJobDetails(int id) {
        Optional<JobPostActivity> jobPostActivity = jobPostActivityRepository.findById(id);
        JobPostActivity jobPostActivity1 = null;
        if (jobPostActivity.isPresent()) {
            jobPostActivity1 = jobPostActivity.get();
        }
        return jobPostActivity1;
    }
}