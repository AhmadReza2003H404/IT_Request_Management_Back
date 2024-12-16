package com.example.request_management.service.impl;

import com.example.request_management.config.security.JwtTokenProvider;
import com.example.request_management.controller.user.create.CreateUserRequest;
import com.example.request_management.domain.AuthenticationResult;
import com.example.request_management.domain.User;
import com.example.request_management.dto.UserDTO;
import com.example.request_management.exception.UserNotFoundException;
import com.example.request_management.repossitory.UserRepository;
import com.example.request_management.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(CreateUserRequest createUserRequest) {
        User user = User.builder()
                .name(createUserRequest.getName())
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .role(User.Role.valueOf(createUserRequest.getRole()))
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

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public AuthenticationResult login(String username, String password) {
        System.out.println("mio1" + username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<User> user = userRepository.findByUsername(username);
            return jwtTokenProvider.generateToken(user.get());

        } catch (Exception e){
            throw e;
        }
    }
}
