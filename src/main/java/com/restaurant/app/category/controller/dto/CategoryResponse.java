package com.restaurant.app.category.controller.dto;

public record CategoryResponse(
        Long categoryId,
        Long positionId,
        String categoryName
) {
}
