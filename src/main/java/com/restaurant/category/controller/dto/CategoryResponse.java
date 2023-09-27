package com.restaurant.category.controller.dto;

public record CategoryResponse(
        Long categoryId,
        Long positionId,
        String categoryName
) {
}
