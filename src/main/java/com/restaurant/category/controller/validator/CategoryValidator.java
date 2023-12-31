package com.restaurant.category.controller.validator;

import com.restaurant.category.controller.dto.CategoryRequest;
import com.restaurant.category.controller.dto.UpdateCategoryRequest;

public class CategoryValidator {

    public boolean isCategoryRequestNotValid(CategoryRequest categoryRequest) {
        return !(categoryRequest instanceof CategoryRequest) ||
                categoryRequest.categoryName() == null || categoryRequest.categoryName().trim().isEmpty()
                || categoryRequest.positionId() == null || categoryRequest.positionId() <= 0;
    }

    public boolean isUpdateCategoryRequestNotValid(UpdateCategoryRequest updateCategoryRequest) {
        return !(updateCategoryRequest instanceof UpdateCategoryRequest) ||
                updateCategoryRequest.categoryName() == null || updateCategoryRequest.categoryName().trim().isEmpty() ||
                updateCategoryRequest.positionId() == null || updateCategoryRequest.positionId() <= 0;
    }
}
