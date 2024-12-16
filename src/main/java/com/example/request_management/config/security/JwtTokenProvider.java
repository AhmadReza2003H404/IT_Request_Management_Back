package com.example.request_management.config.security;


import com.example.request_management.domain.AuthenticationResult;
import com.example.request_management.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${request_management.jwt.secret}")
    private String secret;

    @Value("${request_management.jwt.token-validity-in-minute}")
    private String tokenValidityInMinute;

    public AuthenticationResult generateToken(User user) {
        return AuthenticationResult.builder().accessToken(Jwts.builder()
                        .setSubject(user.getUsername())
                        .claim("role", user.getRole())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(Long.parseLong(tokenValidityInMinute)))))
                        .signWith(SignatureAlgorithm.HS512, secret)
                        .compact())
                .build();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getRole(String token){
       return (String) Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}