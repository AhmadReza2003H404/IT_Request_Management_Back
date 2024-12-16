package com.example.request_management.config.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserServiceImpl implements OnlineUser{
    private Long userId;
    private String username;
    private String password;
    private String title;
    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
