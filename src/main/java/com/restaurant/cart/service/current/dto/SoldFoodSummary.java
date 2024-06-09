package com.restaurant.cart.service.current.dto;

public record SoldFoodSummary(
        String foodName,
        Double price,
        Integer quantitySold,
        Double totalValue,
        Long categoryId,
        String categoryName,
        Long positionId
) {
}
