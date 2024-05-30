package com.mananluvtocode.jobportal.services;

import com.mananluvtocode.jobportal.entity.JobPostActivity;
import com.mananluvtocode.jobportal.entity.JobSeekerProfile;
import com.mananluvtocode.jobportal.entity.JobSeekerSave;
import com.mananluvtocode.jobportal.repository.JobSeekerSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {
    private final JobSeekerSaveRepository seekerSaveRepository;

    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.seekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userId) {
        return seekerSaveRepository.findByJobSeekerProfile(userId);
    }

    public List<JobSeekerSave> getJobCandidates(JobPostActivity job) {
        return seekerSaveRepository.findByJobPostActivity(job);
    }

    public void addNew(JobSeekerSave jobSeekerSave) {
        seekerSaveRepository.save(jobSeekerSave);
    }
}
