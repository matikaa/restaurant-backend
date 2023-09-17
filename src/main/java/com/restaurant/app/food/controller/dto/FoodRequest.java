package com.restaurant.app.food.controller.dto;

public record FoodRequest(
        Long positionId,
        String foodName,
        Double foodPrice
) {
}
