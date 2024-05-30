package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.*;
import com.mananluvtocode.jobportal.repository.JobPostActivityRepository;
import com.mananluvtocode.jobportal.services.*;
import org.hibernate.validator.internal.constraintvalidators.hv.Mod10CheckValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobSeekerApplyController {
    private final JobPostActivityService jobPostActivityService;
    private final UserService userService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobseekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

    @Autowired
    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UserService userService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobseekerSaveService, RecruiterProfileService recruiterProfileService, JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.userService = userService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobseekerSaveService = jobseekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }


    @GetMapping("/job-details-apply/{id}")
    public String displayDetailsForJob(@PathVariable("id") int id, Model themodel) {
        JobPostActivity jobDetails = jobPostActivityService.getJobDetails(id);
        // getting the jobSeekers who have applied for this job
        List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getJobCandidates(jobDetails);
        // get the list of the jobSeekers who have applied for this job.
        List<JobSeekerSave> jobSeekerSaveList = jobseekerSaveService.getJobCandidates(jobDetails);

        // getting the reference for the user logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                RecruiterProfile recruiterProfile = recruiterProfileService.getCurrentRecruiterProfile();
                if (recruiterProfile != null) {
                    themodel.addAttribute("applyList", jobSeekerApplyList);
                }
            } else {
                JobSeekerProfile currentSeekerProfile = jobSeekerProfileService.getCurrentSeekerProfile();
                if (currentSeekerProfile != null) {
                    boolean existed = false;
                    boolean saved = false;
                    for (JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                        if (Objects.equals(jobSeekerApply.getJobSeekerProfile().getUserAccountId(), currentSeekerProfile.getUserAccountId())) {
                            existed = true;
                            break;
                        }
                    }
                    // check the saved list of the jobs for that particular user
                    for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
                        if (jobSeekerSave.getJobSeekerProfile().getUserAccountId() == currentSeekerProfile.getUserAccountId()) {
                            saved = true;
                            break;
                        }
                    }
                    themodel.addAttribute("alreadyApplied", existed);
                    themodel.addAttribute("alreadySaved", saved);
                }
            }
        }
        JobSeekerApply jobSeekerApply = new JobSeekerApply();
        themodel.addAttribute("applyJob", jobSeekerApply);
        themodel.addAttribute("jobDetails", jobDetails);
        themodel.addAttribute("user", userService.getCurrentUserProfile());
        return "job-details";
    }

    // for persisting the applied job we have to create the new mapping

    @PostMapping("/job-details/apply/{id}")
    public String applied(@PathVariable("id") int id, JobSeekerApply jobSeekerApply) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            JobSeekerProfile jobSeekerProfile = jobSeekerProfileService.getCurrentSeekerProfile();
            JobPostActivity jobPostActivity = jobPostActivityService.getJobDetails(id);
            if (jobSeekerProfile != null && jobPostActivity != null) {
                jobSeekerApply.setJobSeekerProfile(jobSeekerProfile);
                jobSeekerApply.setJobPostActivity(jobPostActivity);
                jobSeekerApply.setApplyDate(new Date());
            } else {
                throw new RuntimeException("User not found Exception");
            }
            JobSeekerApply jobSaved = jobSeekerApplyService.addNew(jobSeekerApply);
        }
        return "redirect:/dashboard/";
    }

    // for doing the editing of the job for doing the work.
    @PostMapping("dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int postid, Model themodel) {
        JobPostActivity jobDetails = jobPostActivityService.getJobDetails(postid);
        themodel.addAttribute("jobPostActivity", jobDetails);
        themodel.addAttribute("user", userService.getCurrentUserProfile());
        return "add-jobs";
    }
}
