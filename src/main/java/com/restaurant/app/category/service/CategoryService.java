package com.restaurant.app.category.service;

import com.restaurant.app.category.controller.dto.CategoryRequest;
import com.restaurant.app.category.controller.dto.UpdateCategory;
import com.restaurant.app.category.service.dto.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    void delete(Long categoryId);

    Optional<Category> findById(Long categoryId);

    List<Category> getAll();

    Category insert(CategoryRequest categoryRequest);

    Optional<Category> update(UpdateCategory updateCategory);

    boolean existsByCategoryId(Long categoryId);

    boolean existsByPositionId(Long positionId);
}
