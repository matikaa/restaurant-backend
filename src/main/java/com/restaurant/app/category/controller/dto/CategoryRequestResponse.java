package com.restaurant.app.category.controller.dto;

public record CategoryRequestResponse(
        Long categoryId,
        Long positionId,
        String categoryName
) {
}
