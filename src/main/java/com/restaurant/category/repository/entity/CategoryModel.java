package com.restaurant.category.repository.entity;

public record CategoryModel(
        Long categoryId,
        Long positionId,
        String categoryName
) {
}
