package com.example.request_management.service.impl;

import com.example.request_management.repossitory.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("miioooo" + username);
//        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        return new org.springframework.security.core.userdetails.User(
                "ahmad",
                "$2a$10$A7aUHRwfphqgZzzy6W5fgeyQOPKgaN3ueSduT/tfxF.wDNYhVuUWu",
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
    }
}
