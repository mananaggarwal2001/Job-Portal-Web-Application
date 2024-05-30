package com.mananluvtocode.jobportal.services;

import com.mananluvtocode.jobportal.entity.JobPostActivity;
import com.mananluvtocode.jobportal.entity.JobSeekerApply;
import com.mananluvtocode.jobportal.entity.JobSeekerProfile;
import com.mananluvtocode.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {
    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidatesJob(JobSeekerProfile userAccountId) {
        return jobSeekerApplyRepository.findByJobSeekerProfile(userAccountId);
    }

    public List<JobSeekerApply> getJobCandidates(JobPostActivity job) {
        return jobSeekerApplyRepository.findByJobPostActivity(job);
    }

    public JobSeekerApply addNew(JobSeekerApply jobSeekerApply) {
        return jobSeekerApplyRepository.save(jobSeekerApply);
    }
}
