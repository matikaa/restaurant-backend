package com.restaurant.cart.controller.dto;

import java.util.List;

public record CartStatisticResponse(
        Double value,
        List<SoldFoodSummaryResponse> soldFoodSummaries
) {
}
