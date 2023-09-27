package com.restaurant.food.controller.dto;

public record FoodRequestUpdate(
        Long categoryId,
        Long positionId,
        String foodName,
        Double foodPrice
) {
}
