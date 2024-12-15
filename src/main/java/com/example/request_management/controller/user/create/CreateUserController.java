package com.example.request_management.controller.user.create;

import com.example.request_management.dto.UserDTO;
import com.example.request_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateUserController {

    private final UserService userService;

    @PostMapping("/create-user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest createUserRequest) {
        UserDTO response = userService.createUser(createUserRequest);
        return CreateUserResponse.builder()
                .name(response.getName())
                .build();
    }
}
