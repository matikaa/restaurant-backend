package com.restaurant.app.cart.repository.current.dto;

import java.util.List;

public record CartModel(
        Long cartId,
        Long userId,
        Boolean loyaltyCard,
        Double cartValue,
        List<String> food,
        List<Double> foodPrice
) {
}
