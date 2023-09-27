package com.restaurant.jwt.repository;

import com.restaurant.jwt.repository.dto.JwtModel;

public interface JwtRepository {

    JwtModel insert(JwtModel jwtModel);

    void deleteByToken(String token);

    boolean findByToken(String token);
}
