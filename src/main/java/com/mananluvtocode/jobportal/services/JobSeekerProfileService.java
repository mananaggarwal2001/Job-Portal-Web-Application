package com.mananluvtocode.jobportal.services;

import com.mananluvtocode.jobportal.controller.JobSeekerProfileController;
import com.mananluvtocode.jobportal.entity.JobSeekerProfile;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.repository.JobSeekerProfileRepository;
import com.mananluvtocode.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {
    JobSeekerProfileRepository jobSeekerProfileRepository;
    UserRepository userRepository;

    @Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UserRepository userRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.userRepository = userRepository;
    }

    public JobSeekerProfile saveProfile(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public Optional<JobSeekerProfile> findjobSeekerProfile(Integer jobSeekerId) {
        return jobSeekerProfileRepository.findById(jobSeekerId);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<JobSeekerProfile> jobSeekerProfile = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Couldn't find the given username"));
            jobSeekerProfile = jobSeekerProfileRepository.findById(user.getId());
        }
        JobSeekerProfile currentProfile = null;
        if (jobSeekerProfile.isPresent()) {
            currentProfile = jobSeekerProfile.get();
        }
        return currentProfile;
    }
}
