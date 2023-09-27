package com.restaurant.cart.repository.current;

import com.restaurant.cart.repository.current.dto.CartModel;
import com.restaurant.common.Status;
import com.restaurant.food.service.dto.Food;

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
