package com.restaurant.jwt.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class JwtEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long jwtId;

    private String token;

    public Long getJwtId() {
        return jwtId;
    }

    public void setJwtId(Long jwtId) {
        this.jwtId = jwtId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
