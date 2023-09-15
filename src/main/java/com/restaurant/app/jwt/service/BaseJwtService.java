package com.restaurant.app.jwt.service;

import com.restaurant.app.jwt.repository.dto.JwtModel;
import com.restaurant.app.jwt.repository.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.restaurant.app.response.ConstantValues.TIME;

public class BaseJwtService implements JwtService {

    private final JwtRepository jwtRepository;

    private final String jwtSecret;

    public BaseJwtService(JwtRepository jwtRepository, String jwtSecret) {
        this.jwtRepository = jwtRepository;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String email) {
        var token = Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        jwtRepository.insert(new JwtModel(token));
        return token;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token) && isUserLoggedOut(token));
    }

    @Override
    public void deleteToken(String token) {
        jwtRepository.deleteByToken(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isUserLoggedOut(String token) {
        return jwtRepository.findByToken(token);
    }
}
