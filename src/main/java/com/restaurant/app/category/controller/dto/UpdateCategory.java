package com.restaurant.app.category.controller.dto;

public record UpdateCategory(
        Long categoryId,
        Long positionId,
        String categoryName
) {
}
