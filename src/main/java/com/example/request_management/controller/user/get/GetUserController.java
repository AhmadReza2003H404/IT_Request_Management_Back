package com.example.request_management.controller.user.get;

import com.example.request_management.dto.UserDTO;
import com.example.request_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetUserController {
    private final UserService userService;

    @GetMapping("users/{userId}")
    GetUserResponse getUser(@PathVariable("userId") Long userId){
        UserDTO response = userService.getUser(userId);
        return GetUserResponse.builder()
                .name(response.getName())
                .build();
    }
}
