package com.mananluvtocode.jobportal.repository;

import com.mananluvtocode.jobportal.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    // leave it empty as jpa will provide the crud operation for free and for doing the further stuffs.

}