package com.example.request_management.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResult {
    private String accessToken;
}
