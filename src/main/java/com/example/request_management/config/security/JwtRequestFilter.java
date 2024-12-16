package com.example.request_management.config.security;

import com.example.request_management.config.exceprtion.ErrorContent;
import com.example.request_management.domain.User;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

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
        String[] errorMessageItems = errorMessage.split("#");
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(ErrorContent.builder()
                .message(errorMessageItems[0])
                .code(Integer.parseInt(errorMessageItems[1]))
                .fields(Collections.emptyList())
                .build());
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        logger.error("Authorization header: " + authorizationHeader);

        String username = null;
        String jwtToken = null;

        // Extract token from the Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
            try {
                // Extract username from token
                username = jwtTokenProvider.extractUsername(jwtToken);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired", e);  // Use logger instead of System.out
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
                return;  // End the filter chain as the token is invalid
            } catch (Exception e) {
                logger.error("Error while extracting JWT token", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error while extracting JWT token");
                return;  // End the filter chain if there is any other error
            }
        } else {
            logger.warn("Authorization header is missing or does not contain Bearer token");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or does not contain Bearer token");
            return;  // End the filter chain if there's no authorization header
        }

        // If username is valid and not already authenticated, authenticate the request
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {
                User user = (User) userDetails;  // Assuming userDetails is your User entity
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

                // Manually set the authorities in the authentication token
                var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singletonList(authority));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                logger.warn("JWT Token is not valid");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is not valid");
                return;  // End the filter chain if the token is invalid
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }


}
