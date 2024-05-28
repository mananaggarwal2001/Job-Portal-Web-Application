package com.mananluvtocode.jobportal.services;

import com.mananluvtocode.jobportal.entity.Users;
import com.mananluvtocode.jobportal.repository.UserRepository;
import com.mananluvtocode.jobportal.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users loginUser = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Couldn't Found the given Username"));
        return new CustomUserDetails(loginUser);
    }
}
