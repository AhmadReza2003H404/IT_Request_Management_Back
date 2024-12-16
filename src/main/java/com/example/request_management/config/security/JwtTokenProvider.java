package com.example.request_management.config.security;


import com.example.request_management.config.exceprtion.TokenExpiredException;
import com.example.request_management.domain.AuthenticationResult;
import com.example.request_management.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtTokenProvider implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${request_management.jwt.secret}")
    private String secret;

    @Value("${request_management.jwt.token-validity-in-minute}")
    private String tokenValidityInMinute;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret.getBytes()) // Ensure the key is in byte format
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }



    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

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

    public String extractUsername(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }


}