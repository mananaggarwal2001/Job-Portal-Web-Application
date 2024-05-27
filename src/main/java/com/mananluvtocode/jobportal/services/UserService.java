package com.mananluvtocode.jobportal.services;

import com.mananluvtocode.jobportal.entity.JobSeekerProfile;
import com.mananluvtocode.jobportal.entity.RecruiterProfile;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.repository.JobSeekerProfileRepository;
import com.mananluvtocode.jobportal.repository.RecruiterProfileRepository;
import com.mananluvtocode.jobportal.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserRepository userRepository;
    private JobSeekerProfileRepository jobSeekerProfileRepository;
    private RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    public UserService(UserRepository userRepository, JobSeekerProfileRepository jobSeekerProfileRepository, RecruiterProfileRepository recruiterProfileRepository) {
        this.userRepository = userRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    public Users addNewUser(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        Users savedusers = userRepository.save(users);
        long userTypeId = savedusers.getUserTypeId().getUserTypeId();
        if (userTypeId == 1) {
            recruiterProfileRepository.save(new RecruiterProfile(savedusers));
        } else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedusers));
        }
        return savedusers;
    }
}
