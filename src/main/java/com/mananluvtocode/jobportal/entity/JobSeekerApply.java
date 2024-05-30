package com.mananluvtocode.jobportal.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "job_seeker_apply", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "user_id", "job"
        })
})
public class JobSeekerApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_account_id")
    private JobSeekerProfile jobSeekerProfile;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job", referencedColumnName = "job_post_id")
    private JobPostActivity jobPostActivity;

    @Column(name = "apply_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date applyDate;

    @Column(name = "cover_letter")
    @Length(max = 255)
    private String coverLetter;

    public JobSeekerApply() {
    }

    public JobSeekerApply(Integer id, JobSeekerProfile jobSeekerProfile, JobPostActivity jobPostActivity, Date applyDate, String coverLetter) {
        this.id = id;
        this.jobSeekerProfile = jobSeekerProfile;
        this.jobPostActivity = jobPostActivity;
        this.applyDate = applyDate;
        this.coverLetter = coverLetter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JobSeekerProfile getJobSeekerProfile() {
        return jobSeekerProfile;
    }

    public void setJobSeekerProfile(JobSeekerProfile jobSeekerProfile) {
        this.jobSeekerProfile = jobSeekerProfile;
    }

    public JobPostActivity getJobPostActivity() {
        return jobPostActivity;
    }

    public void setJobPostActivity(JobPostActivity jobPostActivity) {
        this.jobPostActivity = jobPostActivity;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public @Length(max = 255) String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(@Length(max = 255) String coverLetter) {
        this.coverLetter = coverLetter;
    }

    @Override
    public String toString() {
        return "JobSeekerApply{" +
                "id=" + id +
                ", jobSeekerProfile=" + jobSeekerProfile +
                ", jobPostActivity=" + jobPostActivity +
                ", applyDate=" + applyDate +
                ", coverLetter='" + coverLetter + '\'' +
                '}';
    }
}
