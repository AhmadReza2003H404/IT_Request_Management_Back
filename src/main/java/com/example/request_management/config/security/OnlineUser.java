package com.example.request_management.config.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface OnlineUser extends UserDetails {
    Long getId();

    String getTitle();
}
