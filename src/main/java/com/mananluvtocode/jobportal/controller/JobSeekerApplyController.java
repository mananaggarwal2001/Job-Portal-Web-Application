package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.JobPostActivity;
import com.mananluvtocode.jobportal.repository.JobPostActivityRepository;
import com.mananluvtocode.jobportal.services.JobPostActivityService;
import com.mananluvtocode.jobportal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JobSeekerApplyController {
    private final JobPostActivityService jobPostActivityService;
    private final UserService userService;

    @Autowired
    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UserService userService) {
        this.jobPostActivityService = jobPostActivityService;
        this.userService = userService;
    }


    @GetMapping("/job-details-apply/{id}")
    public String displayDetailsForJob(@PathVariable("id") int id, Model themodel) {
        JobPostActivity jobDetails = jobPostActivityService.getJobDetails(id);
        themodel.addAttribute("jobDetails", jobDetails);
        themodel.addAttribute("user", userService.getCurrentUserProfile());
        return "job-details";
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
