package com.mananluvtocode.jobportal.util;

import com.mananluvtocode.jobportal.entity.UserType;
import com.mananluvtocode.jobportal.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Users findedUser;

    public CustomUserDetails(Users findedUser) {
        this.findedUser = findedUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserType userType = findedUser.getUserTypeId();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userType.getUserTypeName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return findedUser.getPassword();
    }

    @Override
    public String getUsername() {
        return findedUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
