package com.restaurant.app.food.controller.dto;

public record FoodRequestUpdate(
        Long categoryId,
        Long positionId,
        String foodName,
        Integer foodPrice
) {
}
