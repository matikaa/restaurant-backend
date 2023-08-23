package com.restaurant.app.category.controller.dto;

import java.util.List;

public record CategoryListResponse(
        List<CategoryResponse> categoryResponses
) {
}
