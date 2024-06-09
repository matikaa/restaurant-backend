package com.restaurant.cart.service.current;

import com.restaurant.cart.controller.dto.OrderDate;
import com.restaurant.cart.service.current.dto.Cart;
import com.restaurant.cart.service.current.dto.SoldFoodSummary;
import com.restaurant.cart.service.delivered.dto.CartDelivered;
import com.restaurant.common.Status;
import com.restaurant.food.service.dto.Food;
import com.restaurant.user.service.dto.User;

import java.util.List;
import java.util.Optional;

public interface CartService {

    Optional<Cart> addFoodToCart(User user, Food food);

    Optional<Cart> getCart(Long userId);

    Optional<Cart> getInDeliveryCart(Long userId);

    Status confirmAnOrder(Long userId);

    void changeStatus(Long userId);

    Status getOrderStatus(Long userId);

    List<CartDelivered> getAllDeliveredCart(Long userId);

    Double getOverallCartValue(Long userId);

    Status cancelOrder(Long userId);

    void cancelSpecificOrder(Long userId, Food food);

    boolean existsFoodByName(Long userId, String foodName);

    List<SoldFoodSummary> getOverallSoldFood(OrderDate orderDate);

    Double getValueOfAllOrders(OrderDate orderDate);
}
