package com.restaurant.app.cart.repository.current;

import com.restaurant.app.cart.repository.current.dto.CartModel;
import com.restaurant.app.food.service.dto.Food;
import com.restaurant.app.common.Status;

import java.util.Optional;

public interface CartRepository {

    Double findValueOfCartByUserId(Long userId);

    Optional<CartModel> findCartByUserId(Long userId);

    Optional<CartModel> addToCart(Long userId, Double foodValue, Food food, Boolean loyaltyCard);

    boolean existsByUserId(Long userId);

    void insert(Long userId);

    void changeStatusToInDelivery(Long userId);

    void confirmAnOrder(Long userId);

    Status getStatus(Long userId);

    void deleteOrder(Long userId);

    void deleteSpecificOrder(Long userId, Food food);

    boolean existsFoodInOrder(Long userId, String foodName);
}
