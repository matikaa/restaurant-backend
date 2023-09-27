package com.restaurant.cart.repository.current;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);

    Optional<CartEntity> findCartEntityByUserId(Long userId);
}
