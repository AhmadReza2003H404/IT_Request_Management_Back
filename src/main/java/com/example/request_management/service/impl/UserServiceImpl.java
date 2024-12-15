package com.example.request_management.service.impl;

import com.example.request_management.controller.user.create.CreateUserRequest;
import com.example.request_management.domain.User;
import com.example.request_management.dto.UserDTO;
import com.example.request_management.exception.UserNotFoundException;
import com.example.request_management.repossitory.UserRepository;
import com.example.request_management.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(CreateUserRequest createUserRequest) {
        User user = User.builder()
                .name(createUserRequest.getName())
                .build();
        userRepository.save(user);
        return UserDTO.builder()
                .name(user.getName())
                .build();
    }

    @Override
    public Page<UserDTO> getUsers(String name, Integer pageSize, Integer pageIndex) {
        Page<User> users = userRepository.getUsersByFilters(name, PageRequest.of(pageIndex, pageSize));

        return new PageImpl<>(
                users.getContent().stream()
                        .map(user -> UserDTO.builder().name(user.getName()).build())
                        .collect(Collectors.toList()),
                PageRequest.of(pageIndex, pageSize),
                users.getTotalElements()
        );
    }

    @Override
    public UserDTO getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserDTO.builder()
                .name(user.getName())
                .build();
    }
}
