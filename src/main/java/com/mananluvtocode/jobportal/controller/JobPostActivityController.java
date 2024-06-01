package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.*;
import com.mananluvtocode.jobportal.services.JobPostActivityService;
import com.mananluvtocode.jobportal.services.JobSeekerApplyService;
import com.mananluvtocode.jobportal.services.JobSeekerSaveService;
import com.mananluvtocode.jobportal.services.UserService;
import org.hibernate.validator.internal.constraintvalidators.bv.time.past.PastValidatorForReadableInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {
    private final UserService userService;
    private JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobPostActivityController(UserService userService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.userService = userService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    // this is for doing the searching for the job for doing the things readily and for getting the results.
    @GetMapping("/dashboard/")
    public String searchJobs(Model themodel
            , @RequestParam(value = "job", required = false) String job
            , @RequestParam(value = "location", required = false) String location
            , @RequestParam(value = "partTime", required = false) String partTime
            , @RequestParam(value = "fullTime", required = false) String fullTime
            , @RequestParam(value = "freelance", required = false) String freelance
            , @RequestParam(value = "remoteOnly", required = false) String remoteOnly
            , @RequestParam(value = "officeOnly", required = false) String officeOnly
            , @RequestParam(value = "partialRemote", required = false) String partialRemote
            , @RequestParam(value = "today", required = false) boolean today
            , @RequestParam(value = "days7", required = false) boolean days7
            , @RequestParam(value = "days30", required = false) boolean days30
    ) {
        themodel.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        themodel.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        themodel.addAttribute("freelance", Objects.equals(freelance, "Freelance"));
        themodel.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        themodel.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        themodel.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));
        themodel.addAttribute("today", today);
        themodel.addAttribute("days7", days7);
        themodel.addAttribute("days30", days30);
        themodel.addAttribute("job", job);
        themodel.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPostActivities = null;


        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;
        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }

        // if everything is false then show each and every job for the job seeker.

        if (!dateSearchFlag && !type && !remote && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPostActivities = jobPostActivityService.getAllJobs();
        } else {
            jobPostActivities = jobPostActivityService.searchForJobs(job, location, Arrays.asList(partTime, fullTime, freelance), Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
        }

        Object currentUserProfile = userService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String getUsername = authentication.getName();
            themodel.addAttribute("username", getUsername);
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                List<RecruiterJobsDTO> recruiterJobsDTOS = jobPostActivityService.recruiterJobsDTOList(((RecruiterProfile) currentUserProfile).getUserAccountId());
                themodel.addAttribute("jobPost", recruiterJobsDTOS);
            } else {
                List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
                // list of jobs that has being saved by the candidate already in the past.
                List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
                boolean isExist;
                boolean saved;
                // loop through each job for checking whether the job that we have either applied or saved for that particular job.
                for (JobPostActivity jobActivity : jobPostActivities) {
                    isExist = false;
                    saved = false;
                    for (JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                        if (Objects.equals(jobActivity.getJobPostId(), jobSeekerApply.getJobPostActivity().getJobPostId())) {
                            jobActivity.setIsActive(true);
                            isExist = true;
                            break;
                        }
                    }

                    for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
                        if (Objects.equals(jobActivity.getJobPostId(), jobSeekerSave.getJobPostActivity().getJobPostId())) {
                            jobActivity.setIsSaved(true);
                            saved = true;
                            break;
                        }
                    }

                    if (!isExist) {
                        jobActivity.setIsActive(false);
                    }
                    if (!saved) {
                        jobActivity.setIsSaved(false);
                    }
                    themodel.addAttribute("jobPost", jobPostActivities);
                }
            }
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
        return "redirect:/dashboard/";
    }

    // implementing the feature for the global search
    @GetMapping("global-search/")
    public String globalSearch(Model themodel
            , @RequestParam(value = "job", required = false) String job
            , @RequestParam(value = "location", required = false) String location
            , @RequestParam(value = "partTime", required = false) String partTime
            , @RequestParam(value = "fullTime", required = false) String fullTime
            , @RequestParam(value = "freelance", required = false) String freelance
            , @RequestParam(value = "remoteOnly", required = false) String remoteOnly
            , @RequestParam(value = "officeOnly", required = false) String officeOnly
            , @RequestParam(value = "partialRemote", required = false) String partialRemote
            , @RequestParam(value = "today", required = false) boolean today
            , @RequestParam(value = "days7", required = false) boolean days7
            , @RequestParam(value = "days30", required = false) boolean days30) {

        themodel.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        themodel.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        themodel.addAttribute("freelance", Objects.equals(freelance, "Freelance"));
        themodel.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        themodel.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        themodel.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));
        themodel.addAttribute("today", today);
        themodel.addAttribute("days7", days7);
        themodel.addAttribute("days30", days30);
        themodel.addAttribute("job", job);
        themodel.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPostActivities = null;


        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;
        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }
        if (!dateSearchFlag && !type && !remote && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPostActivities = jobPostActivityService.getAllJobs();
        } else {
            jobPostActivities = jobPostActivityService.searchForJobs(job, location, Arrays.asList(partTime, fullTime, freelance), Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
        }

        themodel.addAttribute("jobPost", jobPostActivities);
        return "global-search";
    }
}
