package com.mananluvtocode.jobportal.controller;

import com.mananluvtocode.jobportal.entity.UserType;
import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.services.UserTypeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class UsersController {
    private final UserTypeServices userTypeServices;

    @Autowired
    public UsersController(UserTypeServices userTypeServices) {
        this.userTypeServices = userTypeServices;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model themodel) {
        List<UserType> userTypeList = userTypeServices.getAll();
        themodel.addAttribute("getAllRoles", userTypeList);
        themodel.addAttribute("user", new Users());
        return "register";
    }

}
