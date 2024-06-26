package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.JobSeekerProfile;
import com.mananluvtocode.jobportal.entity.Skills;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.repository.UserRepository;
import com.mananluvtocode.jobportal.services.JobSeekerProfileService;
import com.mananluvtocode.jobportal.services.UserService;
import com.mananluvtocode.jobportal.util.FileDownloadUtil;
import com.mananluvtocode.jobportal.util.FileSaveUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {
    private final UserService userService;
    private JobSeekerProfileService jobSeekerProfileService;
    private UserRepository userRepository;

    @Autowired
    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UserRepository userRepository, UserService userService) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showJobSeekerProfile(Model model) {
        JobSeekerProfile jobSeekerProfile = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills = new ArrayList<>();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Users users = userRepository.findByEmail(currentUserName).orElseThrow(() -> new UsernameNotFoundException("Couldn't Find the given username"));
            Optional<JobSeekerProfile> seekerprofile = jobSeekerProfileService.findjobSeekerProfile(users.getId());
            if (seekerprofile.isPresent()) {
                jobSeekerProfile = seekerprofile.get();
            }
            if (jobSeekerProfile.getSkills().isEmpty()) {
                skills.add(new Skills());
                jobSeekerProfile.setSkills(skills);
            }
            model.addAttribute("skills", skills);
            model.addAttribute("profile", jobSeekerProfile);
        }
        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addjobprofile(Model themodel, @ModelAttribute("profile") JobSeekerProfile jobSeekerProfile, @RequestParam("image") MultipartFile image, @RequestParam("pdf") MultipartFile resume, BindingResult result) throws IOException, SQLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String getCurrentLoggedInUser = authentication.getName();
            Users users = userRepository.findByEmail(getCurrentLoggedInUser).orElseThrow(() -> new UsernameNotFoundException("Couldn't find the user with the given username"));
            jobSeekerProfile.setUserid(users);
            jobSeekerProfile.setUserAccountId(users.getId());
        }
        List<Skills> skillsList = new ArrayList<>();
        themodel.addAttribute("profile", jobSeekerProfile);
        themodel.addAttribute("skills", skillsList);
        for (Skills skill : jobSeekerProfile.getSkills()) {
            skill.setJobSeekerProfile(jobSeekerProfile);
        }
        String imageName = "";
        String resumeName = "";
        System.out.println(image.getOriginalFilename());
        if (!Objects.equals(image.getOriginalFilename(), "")) {
            System.out.println("Image for the multipart file is :- " + image.getOriginalFilename());
            byte[] imagebytes = image.getBytes();
            Blob blob = new SerialBlob(imagebytes);
            jobSeekerProfile.setProfilePhoto(blob);
        }
        if (!Objects.equals(resume.getOriginalFilename(), "")) {
            resumeName = StringUtils.cleanPath(Objects.requireNonNull(resume.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }
        JobSeekerProfile savedProfile = jobSeekerProfileService.saveProfile(jobSeekerProfile);
        try {
            String uploadDir = "jobseeker/" + savedProfile.getUserAccountId();
            if (!Objects.equals(image.getOriginalFilename(), "")) {
                FileSaveUtil.addPhoto(uploadDir, imageName, image);
            }
            if (!Objects.equals(resume.getOriginalFilename(), "")) {
                FileSaveUtil.addPhoto(uploadDir, resumeName, resume);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(savedProfile);
        System.out.println(skillsList);
        return "redirect:/dashboard/";
    }

    // adding the post mapping for viewing the job seeker profile controller for doing the work

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id, Model themodel) {
        Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerProfileService.getOne(id);
        JobSeekerProfile jobSeekerProfile1 = null;
        if (jobSeekerProfile.isPresent()) {
            jobSeekerProfile1 = jobSeekerProfile.get();
        }
        themodel.addAttribute("profile", jobSeekerProfile1);
        return "job-seeker-profile";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam("fileName") String fileName, @RequestParam("userID") Integer userId) {
        FileDownloadUtil fileDownloadUtil = new FileDownloadUtil();
        Resource resource = null;
        try {
            resource = fileDownloadUtil.getFileAsResource("jobseeker/" + userId, fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        if (resource == null) {
            System.out.println(resource);
            return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
        }
        String contentType = "application/octet-stream";
        String headerValue = "attachment; fileName=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    // for displaying the profile photo
    @GetMapping("/displayprofilephoto")
    public ResponseEntity<byte[]> imageResponse() throws SQLException {
        JobSeekerProfile jobSeekerProfile = jobSeekerProfileService.getCurrentSeekerProfile();
        byte[] imagebyte = null;
        imagebyte = jobSeekerProfile.getProfilePhoto().getBytes(1, (int) jobSeekerProfile.getProfilePhoto().length());
        System.out.println(imagebyte.length);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagebyte);
    }
}