package com.restaurant.food.controller.dto;

import java.util.List;

public record FoodListResponse(
        List<FoodResponse> foodResponses
) {
}
