package com.restaurant.cart.repository.delivered;

import com.restaurant.cart.repository.current.dto.CartModel;
import com.restaurant.cart.repository.delivered.dto.CartDeliveredModel;

import java.util.List;
import java.util.Optional;

public interface CartDeliveredRepository {

    boolean existsByUserId(Long userId);

    void insert(Optional<CartModel> cartModel);

    List<CartDeliveredModel> findCartsDeliveredByUserId(Long userId);

    List<CartDeliveredEntity> findAll();

    List<CartDeliveredModel> findAllOrders();
}
