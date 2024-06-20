package com.restaurant.cart.controller.dto;

public record SoldFoodSummaryResponse(
        String foodName,
        Double price,
        Integer quantitySold,
        Double totalValue,
        Long categoryId,
        String categoryName,
        Long positionId
) {
}
