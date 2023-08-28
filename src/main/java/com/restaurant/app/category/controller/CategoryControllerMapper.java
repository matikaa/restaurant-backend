package com.restaurant.app.category.controller;

import com.restaurant.app.category.controller.dto.CategoryRequestResponse;
import com.restaurant.app.category.controller.dto.CategoryResponse;
import com.restaurant.app.category.controller.dto.UpdateCategory;
import com.restaurant.app.category.controller.dto.UpdateCategoryRequest;
import com.restaurant.app.category.service.dto.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryControllerMapper {

    CategoryControllerMapper INSTANCE = Mappers.getMapper(CategoryControllerMapper.class);

    CategoryResponse categoryToCategoryResponse(Category category);

    CategoryRequestResponse categoryToCategoryRequestResponse(Category category);

    List<CategoryResponse> categoriesToCategoryResponses(List<Category> categories);

    default UpdateCategory updateCategoryRequestToUpdateCategory(UpdateCategoryRequest updateCategoryRequest, Long categoryId) {
        return new UpdateCategory(categoryId, updateCategoryRequest.positionId(), updateCategoryRequest.categoryName());
    }
}
