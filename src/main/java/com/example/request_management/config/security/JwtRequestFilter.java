package com.example.request_management.config.security;

import com.example.request_management.config.exception.ErrorContent;
import com.example.request_management.config.exception.InvalidTokenException;
import com.example.request_management.config.exception.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MessageSource messageSource;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private final UserDetailsService userDetailsService;

    private static String parseErrorMessage(String errorMessage) throws JsonProcessingException {
        log.error(errorMessage);
        String[] errorMessageItems = errorMessage.split("#");
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(ErrorContent.builder()
                .message(errorMessageItems[0])
                .code(Integer.parseInt(errorMessageItems[1]))
                .fields(Collections.emptyList())
                .build());
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                String role = jwtTokenProvider.getRole(token);

                System.out.println("Username: " + username);
                System.out.println("Role: " + role);

                if (username != null && role != null) {
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_"+role)))
                    );
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");

                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.getOutputStream().write(parseErrorMessage(messageSource.getMessage(
                        InvalidTokenException.class.getName(),
                        null, Locale.getDefault()
                )).getBytes("UTF-8"));
                return;

            } catch (ExpiredJwtException e) {
                log.debug("JWT Token has expired");

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getOutputStream().write(parseErrorMessage(messageSource.getMessage(
                        TokenExpiredException.class.getName(),
                        null, Locale.getDefault()
                )).getBytes("UTF-8"));
                return;
            } catch (Exception e){
                log.debug("Something went wrong");

                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setContentType("application/json");
                response.getOutputStream().write(parseErrorMessage(messageSource.getMessage(
                        TokenExpiredException.class.getName(),
                        null, Locale.getDefault()
                )).getBytes("UTF-8"));
                return;
            }
        } else {
            System.out.println("No Authorization header found");
        }

        filterChain.doFilter(request, response);
    }
}
