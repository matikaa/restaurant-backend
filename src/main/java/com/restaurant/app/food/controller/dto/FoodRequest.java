package com.restaurant.app.food.controller.dto;

public record FoodRequest(
        Long categoryId,
        Long positionId,
        String foodName,
        Integer foodPrice
) {
}
