package com.restaurant.food.controller.dto;

public record FoodRequestResponse(
        Long foodId,
        Long categoryId,
        Long positionId,
        String foodName,
        Double foodPrice
) {
}
