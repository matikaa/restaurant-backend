package com.restaurant.app.cart.repository.delivered;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartDeliveredJpaRepository extends JpaRepository<CartDeliveredEntity, Long> {

    boolean existsByUserId(Long userId);

    List<CartDeliveredEntity> findCartDeliveredEntitiesByUserId(Long userId);
}
