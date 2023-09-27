package com.restaurant.food.controller.dto;

public record FoodRequest(
        Long positionId,
        String foodName,
        Double foodPrice
) {
}
