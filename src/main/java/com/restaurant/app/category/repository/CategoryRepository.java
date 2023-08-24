package com.restaurant.app.category.repository;

import com.restaurant.app.category.controller.dto.CategoryRequest;
import com.restaurant.app.category.controller.dto.UpdateCategory;
import com.restaurant.app.category.repository.entity.CategoryModel;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Optional<CategoryModel> findCategoryById(Long categoryId);

    Optional<CategoryModel> update(UpdateCategory updateCategory);

    void delete(Long categoryId);

    CategoryModel save(CategoryRequest categoryRequest);

    List<CategoryModel> findAll();

    boolean exists(Long categoryId);
}
