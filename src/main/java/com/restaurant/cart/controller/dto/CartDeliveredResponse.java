package com.restaurant.cart.controller.dto;

import java.util.List;

public record CartDeliveredResponse(
        Double overallCartValue,
        List<CartResponse> cartResponses
) {
}
