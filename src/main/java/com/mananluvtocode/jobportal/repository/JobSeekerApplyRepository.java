package com.mananluvtocode.jobportal.repository;

import com.mananluvtocode.jobportal.entity.JobPostActivity;
import com.mananluvtocode.jobportal.entity.JobSeekerApply;
import com.mananluvtocode.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {
    List<JobSeekerApply> findByJobSeekerProfile(JobSeekerProfile UserId);
    List<JobSeekerApply> findByJobPostActivity(JobPostActivity job);
}
