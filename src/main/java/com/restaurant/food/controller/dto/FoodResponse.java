package com.restaurant.food.controller.dto;

public record FoodResponse(
        Long foodId,
        Long categoryId,
        Long positionId,
        String foodName,
        Double foodPrice
) {
}
