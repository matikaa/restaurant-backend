package com.restaurant.cart.service.delivered.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record CartDelivered(
        Long cartId,
        Long userId,
        Boolean loyaltyCard,
        Double cartValue,
        List<String> food,
        List<Double> foodPrice,
        ZonedDateTime orderDate
) {
}
