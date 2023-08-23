package com.restaurant.app.category.controller.dto;

public record CategoryRequest(
        Long positionId,
        String categoryName
) {
}
