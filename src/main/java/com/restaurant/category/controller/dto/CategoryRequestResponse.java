package com.restaurant.category.controller.dto;

public record CategoryRequestResponse(
        Long categoryId,
        Long positionId,
        String categoryName
) {
}
