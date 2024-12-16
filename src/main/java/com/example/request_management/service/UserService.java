package com.example.request_management.service;

import com.example.request_management.controller.user.create.CreateUserRequest;
import com.example.request_management.domain.AuthenticationResult;
import com.example.request_management.domain.User;
import com.example.request_management.dto.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDTO createUser(CreateUserRequest createUserRequest);

    Page<UserDTO> getUsers(String name, Integer pageSize, Integer pageIndex);

    UserDTO getUser(Long userId);

    User findByUsername(String username);

    AuthenticationResult login(String username, String password);
}
