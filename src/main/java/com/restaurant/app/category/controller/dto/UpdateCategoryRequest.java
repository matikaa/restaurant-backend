package com.restaurant.app.category.controller.dto;

public record UpdateCategoryRequest(
        Long positionId,
        String categoryName
) {
}
