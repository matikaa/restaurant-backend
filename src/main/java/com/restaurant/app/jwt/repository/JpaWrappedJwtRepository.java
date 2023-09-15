package com.restaurant.app.jwt.repository;

import com.restaurant.app.jwt.repository.dto.JwtModel;

public class JpaWrappedJwtRepository implements JwtRepository {

    private final JwtJpaRepository jwtJpaRepository;

    private static final JwtRepositoryMapper jwtRepositoryMapper = JwtRepositoryMapper.INSTANCE;

    public JpaWrappedJwtRepository(JwtJpaRepository jwtJpaRepository) {
        this.jwtJpaRepository = jwtJpaRepository;
    }

    @Override
    public JwtModel insert(JwtModel jwtModel) {
        return jwtRepositoryMapper.jwtEntityToJwtModel(
                jwtJpaRepository.save(jwtRepositoryMapper.jwtModelToJwtEntity(jwtModel)));
    }

    @Override
    public boolean findByToken(String token) {
        return jwtJpaRepository.existsByToken(token);
    }

    @Override
    public void deleteByToken(String token) {
        jwtJpaRepository.deleteByToken(token);
    }
}
