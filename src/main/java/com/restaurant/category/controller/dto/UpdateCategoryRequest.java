package com.restaurant.category.controller.dto;

public record UpdateCategoryRequest(
        Long positionId,
        String categoryName
) {
}
