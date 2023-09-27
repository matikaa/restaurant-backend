package com.restaurant.category.service.dto;

public record Category(
        Long categoryId,
        Long positionId,
        String categoryName
) {
}
