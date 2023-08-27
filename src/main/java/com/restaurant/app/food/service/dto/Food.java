package com.restaurant.app.food.service.dto;

public record Food(
        Long foodId,
        Long categoryId,
        Long positionId,
        String foodName,
        Integer foodPrice
) {
}
