package com.restaurant.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    boolean validateToken(String token, UserDetails userDetails);

    String generateToken(String email);

    String extractEmail(String token);

    void deleteToken(String token);
}
