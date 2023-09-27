package com.restaurant.cart.repository.delivered.dto;

import java.util.List;

public record CartDeliveredModel(
        Long cartId,
        Long userId,
        Boolean loyaltyCard,
        Double cartValue,
        List<String> food,
        List<Double> foodPrice
) {
}
