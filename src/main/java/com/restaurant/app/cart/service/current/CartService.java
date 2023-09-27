package com.restaurant.app.cart.service.current;

import com.restaurant.app.cart.service.current.dto.Cart;
import com.restaurant.app.cart.service.delivered.dto.CartDelivered;
import com.restaurant.app.common.Status;
import com.restaurant.app.food.service.dto.Food;
import com.restaurant.app.user.service.dto.User;

import java.util.List;
import java.util.Optional;

public interface CartService {

    Optional<Cart> addFoodToCart(User user, Food food);

    Optional<Cart> getCart(Long userId);

    Status confirmAnOrder(Long userId);

    void changeStatus(Long userId);

    Status getOrderStatus(Long userId);

    List<CartDelivered> getAllDeliveredCart(Long userId);

    Double getOverallCartValue(Long userId);

    Status cancelOrder(Long userId);

    void cancelSpecificOrder(Long userId, Food food);

    boolean existsFoodByName(Long userId, String foodName);
}
