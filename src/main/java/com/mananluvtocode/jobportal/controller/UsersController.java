package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.UserType;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.services.UserService;
import com.mananluvtocode.jobportal.services.UserTypeServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsersController {
    private final UserTypeServices userTypeServices;
    private final UserService userService;

    @Autowired
    public UsersController(UserTypeServices userTypeServices, UserService userService) {
        this.userTypeServices = userTypeServices;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model themodel) {
        List<UserType> userTypeList = userTypeServices.getAll();
        themodel.addAttribute("getAllRoles", userTypeList);
        themodel.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String processRegistrationForm(@Valid @ModelAttribute("user") Users user, BindingResult bindingResult) {
//        System.out.println("user" + user);
        userService.addNewUser(user);
        return "redirect:/dashboard/";
    }

    @GetMapping("/login")
    public String loginUser() {
        return "login";
    }

    @GetMapping("/logout")
    public String logoutUser(HttpServletResponse response, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }
}
