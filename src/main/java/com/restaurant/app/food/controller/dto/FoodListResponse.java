package com.restaurant.app.food.controller.dto;

import java.util.List;

public record FoodListResponse(
        List<FoodResponse> foodResponses
) {
}
