package com.mananluvtocode.jobportal.repository;

import com.mananluvtocode.jobportal.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String emailAddress);
    // leave it empty as jpa will provide the crud operation for free and for doing the further stuffs.
}