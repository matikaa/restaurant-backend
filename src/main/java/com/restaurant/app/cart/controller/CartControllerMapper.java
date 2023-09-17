package com.restaurant.app.cart.controller;

import com.restaurant.app.cart.controller.dto.CartResponse;
import com.restaurant.app.cart.service.current.dto.Cart;
import com.restaurant.app.cart.service.delivered.dto.CartDelivered;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartControllerMapper {

    CartControllerMapper INSTANCE = Mappers.getMapper(CartControllerMapper.class);

    CartResponse cartToCartResponse(Cart cart);

    default List<CartResponse> cartDeliveredToCartDeliveredResponse(List<CartDelivered> cartDelivered) {
        return cartDelivered.stream()
                .map(this::cartDeliveredToCartDeliveredResponse)
                .toList();
    }

    CartResponse cartDeliveredToCartDeliveredResponse(CartDelivered cartDelivered);
}
