package com.restaurant.category.controller.dto;

public record CategoryRequest(
        Long positionId,
        String categoryName
) {
}
