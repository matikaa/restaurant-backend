package com.restaurant.app.cart.service.delivered.dto;

import java.util.List;

public record CartDelivered(
        Long cartId,
        Long userId,
        Boolean loyaltyCard,
        Double cartValue,
        List<String> food,
        List<Double> foodPrice
) {
}
