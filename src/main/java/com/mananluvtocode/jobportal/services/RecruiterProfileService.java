package com.mananluvtocode.jobportal.services;

import com.mananluvtocode.jobportal.entity.RecruiterProfile;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.repository.RecruiterProfileRepository;
import com.mananluvtocode.jobportal.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UserRepository userRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    public Optional<RecruiterProfile> findById(Integer id) {
        return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
        return recruiterProfileRepository.save(recruiterProfile);
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<RecruiterProfile> recruiterProfile = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Users user = userRepository.findByEmail(currentUserName).orElseThrow(() -> new UsernameNotFoundException("Couldn't find the Profile with this username"));
            recruiterProfile = recruiterProfileRepository.findById(user.getId());
        }
        return recruiterProfile.orElse(null);
    }
}