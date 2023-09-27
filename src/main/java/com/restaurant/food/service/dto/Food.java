package com.restaurant.food.service.dto;

public record Food(
        Long foodId,
        Long categoryId,
        Long positionId,
        String foodName,
        Double foodPrice
) {
}
