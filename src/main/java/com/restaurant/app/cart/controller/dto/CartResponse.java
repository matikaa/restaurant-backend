package com.restaurant.app.cart.controller.dto;

import java.util.List;

public record CartResponse(
        Double cartValue,
        List<String> food,
        List<Double> foodPrice
) {
}
