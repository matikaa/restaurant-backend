package com.restaurant.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtJpaRepository extends JpaRepository<JwtEntity, Long> {

    void deleteByToken(String token);

    boolean existsByToken(String token);
}
