package com.restaurant.category.controller.dto;

public record UpdateCategory(
        Long categoryId,
        Long positionId,
        String categoryName
) {
}
