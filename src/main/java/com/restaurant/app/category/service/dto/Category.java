package com.restaurant.app.category.service.dto;

public record Category(
        Long categoryId,
        Long positionId,
        String categoryName
) {
}
