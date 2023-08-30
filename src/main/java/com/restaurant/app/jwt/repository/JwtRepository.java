package com.restaurant.app.jwt.repository;

import com.restaurant.app.jwt.repository.dto.JwtModel;

public interface JwtRepository {

    JwtModel insert(JwtModel jwtModel);

    void deleteByToken(String token);

    boolean findByToken(String token);
}
