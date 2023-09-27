package com.restaurant.services;

import com.restaurant.cart.repository.current.dto.CartModel;
import com.restaurant.cart.service.current.dto.Cart;
import com.restaurant.food.service.dto.Food;
import com.restaurant.user.service.dto.User;

import java.time.LocalDateTime;
import java.util.List;

public class BaseTestUseCase {

    public User getWithUser() {
        return new User(
                1L,
                "michael@gmail.com",
                "Michael",
                LocalDateTime.now(),
                "USER",
                "Wall street 55, 00-355 Florida",
                "0359683552",
                1250D,
                true
        );
    }

    public User getWithoutUser() {
        return new User(
                1L,
                "michael@gmail.com",
                "Michael",
                LocalDateTime.now(),
                "USER",
                "Wall street 55, 00-355 Florida",
                "0359683552",
                25D,
                false
        );
    }

    public Food getFood() {
        return new Food(
                1L,
                1L,
                1L,
                "Beer",
                50D
        );
    }

    public Food getWithoutFood() {
        return new Food(
                1L,
                1L,
                1L,
                "Beer",
                10D
        );
    }

    public CartModel getWithCartModel() {
        return new CartModel(
                1L,
                1L,
                true,
                55D,
                List.of("Beer"),
                List.of(50D)
        );
    }

    public Cart getCartFromCartModel(CartModel cartModel) {
        return new Cart(
                cartModel.cartId(),
                cartModel.userId(),
                cartModel.loyaltyCard(),
                cartModel.cartValue(),
                cartModel.food(),
                cartModel.foodPrice()
        );
    }
}
