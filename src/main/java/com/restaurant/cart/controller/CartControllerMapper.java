package com.restaurant.cart.controller;

import com.restaurant.cart.controller.dto.CartResponse;
import com.restaurant.cart.controller.dto.CartResponseDelivered;
import com.restaurant.cart.controller.dto.SoldFoodSummaryResponse;
import com.restaurant.cart.service.current.dto.Cart;
import com.restaurant.cart.service.current.dto.SoldFoodSummary;
import com.restaurant.cart.service.delivered.dto.CartDelivered;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartControllerMapper {

    CartControllerMapper INSTANCE = Mappers.getMapper(CartControllerMapper.class);

    CartResponse cartToCartResponse(Cart cart);

    default List<CartResponseDelivered> cartDeliveredsToCartResponseDelivereds(List<CartDelivered> cartDelivered) {
        return cartDelivered.stream()
                .map(this::cartDeliveredToCartResponseDelivered)
                .toList();
    }

    CartResponseDelivered cartDeliveredToCartResponseDelivered(CartDelivered cartDelivered);

    List<SoldFoodSummaryResponse> soldFoodSummaryToSoldFoodSummaryResponse(List<SoldFoodSummary> overallSoldFood);
}
