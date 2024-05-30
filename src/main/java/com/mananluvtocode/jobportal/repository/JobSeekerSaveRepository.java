package com.mananluvtocode.jobportal.repository;

import com.mananluvtocode.jobportal.entity.JobPostActivity;
import com.mananluvtocode.jobportal.entity.JobSeekerProfile;
import com.mananluvtocode.jobportal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer> {
    List<JobSeekerSave> findByJobSeekerProfile(JobSeekerProfile userAccountId);

    List<JobSeekerSave> findByJobPostActivity(JobPostActivity job);

}
