package com.mananluvtocode.jobportal.services;

import com.mananluvtocode.jobportal.controller.JobSeekerProfileController;
import com.mananluvtocode.jobportal.entity.JobSeekerProfile;
import com.mananluvtocode.jobportal.repository.JobSeekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {
    JobSeekerProfileRepository jobSeekerProfileRepository;

    @Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
    }

    public JobSeekerProfile saveProfile(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public Optional<JobSeekerProfile> findjobSeekerProfile(Integer jobSeekerId) {
        return jobSeekerProfileRepository.findById(Long.valueOf(jobSeekerId));
    }
}
