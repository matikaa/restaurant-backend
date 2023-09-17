package com.restaurant.app.food.controller.dto;

public record FoodRequestResponse(
        Long foodId,
        Long categoryId,
        Long positionId,
        String foodName,
        Double foodPrice
) {
}
