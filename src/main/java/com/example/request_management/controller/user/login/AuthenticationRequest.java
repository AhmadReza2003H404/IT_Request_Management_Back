package com.example.request_management.controller.user.login;

import lombok.Builder;
import lombok.Data;

// TODO : we can add captcha later
@Builder
@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
