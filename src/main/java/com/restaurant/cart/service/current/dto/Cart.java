package com.restaurant.cart.service.current.dto;

import java.util.List;

public record Cart(
        Long cartId,
        Long userId,
        Boolean loyaltyCard,
        Double cartValue,
        List<String> food,
        List<Double> foodPrice
) {
}
