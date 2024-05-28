package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.RecruiterProfile;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.repository.UserRepository;
import com.mananluvtocode.jobportal.services.RecruiterProfileService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {
    private final UserRepository userRepository;
    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UserRepository userRepository, RecruiterProfileService recruiterProfileService) {
        this.recruiterProfileService = recruiterProfileService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String recruiterProfile(Model themodel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Users user = userRepository.findByEmail(currentUserName).orElseThrow(() -> new UsernameNotFoundException("Couldn't Find The Username"));
            Optional<RecruiterProfile> recruiterProfileService1 = recruiterProfileService.findById(user.getId());
            if (!recruiterProfileService1.isEmpty()) {
                themodel.addAttribute("profile", recruiterProfileService1.get());
            }
        }
        return "recruiter_profile";
    }

    // for adding the recruiter profile and photo for this profile for doing the further stuffs.
}
