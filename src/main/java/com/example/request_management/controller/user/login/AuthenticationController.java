package com.example.request_management.controller.user.login;

import com.example.request_management.domain.AuthenticationResult;
import com.example.request_management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthenticationController {

    private final UserService userService;
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request){
        AuthenticationResult result = userService.login(request.getUsername(), request.getPassword());
        return AuthenticationResponse.builder()
                .token(result.getAccessToken())
                .build();
    }

}
