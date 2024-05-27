package com.mananluvtocode.jobportal.services;

import com.mananluvtocode.jobportal.entity.UserType;
import com.mananluvtocode.jobportal.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeServices {
    private final UserTypeRepository userTypeRepository;

    @Autowired
    public UserTypeServices(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public List<UserType> getAll() {
        return userTypeRepository.findAll();
    }
}
