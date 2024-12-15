package com.example.request_management.controller.createUser;

import com.example.request_management.domain.User;
import com.example.request_management.repossitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateUserController {
    private final UserRepository userRepository;

    public CreateUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/create-user")
    public String createUser() {
        User user = User.builder()
                .name("mio")
                .build();
        userRepository.save(user);
        return "User saved!";
    }
}
