package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.JobPostActivity;
import com.mananluvtocode.jobportal.entity.RecruiterProfile;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.services.JobPostActivityService;
import com.mananluvtocode.jobportal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class JobPostActivityController {
    private final UserService userService;
    private JobPostActivityService jobPostActivityService;

    @Autowired
    public JobPostActivityController(UserService userService, JobPostActivityService jobPostActivityService) {
        this.userService = userService;
        this.jobPostActivityService = jobPostActivityService;
    }

    @GetMapping("/dashboard")
    public String searchJobs(Model themodel) {
        Object currentUserProfile = userService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String getUsername = authentication.getName();
            themodel.addAttribute("username", getUsername);
        }
        themodel.addAttribute("user", currentUserProfile);
        return "dashboard";
    }


    @GetMapping("/dashboard/add")
    // showing the jobPost form for doing the work.
    public String addJob(Model themodel) {
        themodel.addAttribute("jobPostActivity", new JobPostActivity());
        themodel.addAttribute("user", userService.getCurrentUserProfile());
        // then integrating the html form for posting the new job.
        return "add-jobs";
    }

    // for adding the new job for doing the further stuffs.
    @PostMapping("/dashboard/addNew")
    public String addNew(@ModelAttribute("jobPostActivity") JobPostActivity jobPostActivity, Model themodel) {
        // finding the currently logged in user
        Users users = userService.getCurrentUser();
        if (users != null) {
            jobPostActivity.setPostedById(users);
        }
        jobPostActivity.setPostedDate(new Date());
        themodel.addAttribute("jobPostActivity", jobPostActivity);
        JobPostActivity savedJobPost = jobPostActivityService.addNew(jobPostActivity);
        System.out.println(savedJobPost);
        return "redirect:/dashboard";
    }
}
