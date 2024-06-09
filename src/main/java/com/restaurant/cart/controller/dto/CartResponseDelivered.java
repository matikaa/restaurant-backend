package com.restaurant.cart.controller.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record CartResponseDelivered(
        Double cartValue,
        List<String> food,
        List<Double> foodPrice,
        ZonedDateTime orderDate
) {
}
