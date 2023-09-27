package com.restaurant.food.repository.dto;

public record FoodModel(
        Long foodId,
        Long categoryId,
        Long positionId,
        String foodName,
        Double foodPrice
) {
}
