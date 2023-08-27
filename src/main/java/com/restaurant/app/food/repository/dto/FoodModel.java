package com.restaurant.app.food.repository.dto;

public record FoodModel(
        Long foodId,
        Long categoryId,
        Long positionId,
        String foodName,
        Integer foodPrice
) {
}
