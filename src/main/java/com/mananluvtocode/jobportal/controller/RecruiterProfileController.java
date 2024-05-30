package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.RecruiterProfile;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.repository.UserRepository;
import com.mananluvtocode.jobportal.services.RecruiterProfileService;
import com.mananluvtocode.jobportal.util.FileSaveUtil;
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
    @PostMapping("/addNew")
    public String addNewRecruiterDetails(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile, Model themodel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String getName = authentication.getName();
            Users user = userRepository.findByEmail(getName).orElseThrow(() -> new UsernameNotFoundException("Couldn't Find the User profile"));
            recruiterProfile.setUserAccountId(user.getId());
            recruiterProfile.setUserid(user);
        }
        String fileName = "";
        if (!multipartFile.getOriginalFilename().equals("")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        }
        recruiterProfile.setProfilePhoto(fileName);
        RecruiterProfile savedProfile = recruiterProfileService.addNew(recruiterProfile);
        // adding the destination for doing the file upload.
        String uploadDir = "recruiter/" + savedProfile.getUserAccountId();
        try {
            FileSaveUtil.addPhoto(uploadDir, fileName, multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't Save the file");
        }
        return "redirect:/dashboard/";
    }
}
