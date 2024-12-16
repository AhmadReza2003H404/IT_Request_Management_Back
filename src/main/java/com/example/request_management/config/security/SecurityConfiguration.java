package com.example.request_management.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfiguration(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login").permitAll() // Permit /login without authentication
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/login") // Optional: Disable CSRF for /login if necessary
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults()); // Using HTTP Basic Authentication, adjust if you need JWT or another mechanism

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}