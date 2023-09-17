package com.restaurant.app.cart.service.delivered;

import com.restaurant.app.cart.repository.current.dto.CartModel;
import com.restaurant.app.cart.service.delivered.dto.CartDelivered;

import java.util.List;
import java.util.Optional;

public interface CartDeliveredService {

    void addToUser(Optional<CartModel> cartModel);

    List<CartDelivered> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Double sumAll(Long userId);
}
