package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.JobPostActivity;
import com.mananluvtocode.jobportal.entity.JobSeekerProfile;
import com.mananluvtocode.jobportal.entity.JobSeekerSave;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.repository.JobSeekerSaveRepository;
import com.mananluvtocode.jobportal.services.JobPostActivityService;
import com.mananluvtocode.jobportal.services.JobSeekerProfileService;
import com.mananluvtocode.jobportal.services.JobSeekerSaveService;
import com.mananluvtocode.jobportal.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class JobSeekerSaveController {
    private final UserService userService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobSeekerSaveRepository jobSeekerSaveRepository;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;


    public JobSeekerSaveController(UserService userService, JobSeekerProfileService jobSeekerProfileService, JobSeekerSaveRepository jobSeekerSaveRepository, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
        this.userService = userService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }


    @PostMapping("/job-details/save/{id}")
    public String save(@PathVariable("id") int id, JobSeekerSave jobSeekerSave) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentLoggedInUser = authentication.getName();
            Users users = userService.findByEmail(currentLoggedInUser);
            JobSeekerProfile jobSeekerProfile = jobSeekerProfileService.getCurrentSeekerProfile();
            JobPostActivity jobPostActivity = jobPostActivityService.getJobDetails(id);
            System.out.println(jobSeekerProfile);
            System.out.println(jobPostActivity);
            if (jobSeekerProfile != null && jobPostActivity != null) {
                jobSeekerSave.setJobPostActivity(jobPostActivity);
                jobSeekerSave.setJobSeekerProfile(jobSeekerProfile);
            } else {
                throw new RuntimeException("User not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard/";
    }

    // for showing the list of saved jobs for showing the particular jobpost for doing the things right.
    @GetMapping("saved-jobs/")
    public String savedJobs(Model model) {
        List<JobPostActivity> jobPostActivityList = new ArrayList<>();
        Object currentUserProfile = userService.getCurrentUserProfile();
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
        for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
            jobPostActivityList.add(jobSeekerSave.getJobPostActivity());
        }
        model.addAttribute("jobPost", jobPostActivityList);
        model.addAttribute("user", currentUserProfile);
        return "saved-jobs";
    }
}
