package com.restaurant.category.service;

import com.restaurant.category.controller.dto.CategoryRequest;
import com.restaurant.category.controller.dto.UpdateCategory;
import com.restaurant.category.repository.CategoryRepository;
import com.restaurant.category.service.dto.Category;

import java.util.List;
import java.util.Optional;

public class BaseCategoryService implements CategoryService {

    private static final CategoryServiceMapper categoryServiceMapper = CategoryServiceMapper.INSTANCE;
    private final CategoryRepository categoryRepository;

    public BaseCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        return categoryServiceMapper.categoryModelsToCategories(categoryRepository.findAll());
    }

    @Override
    public Category insert(CategoryRequest categoryRequest) {
        return categoryServiceMapper.categoryModelToCategory(categoryRepository.save(categoryRequest));
    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findCategoryById(categoryId)
                .map(categoryServiceMapper::categoryModelToCategory);
    }

    @Override
    public void delete(Long categoryId) {
        categoryRepository.delete(categoryId);
    }

    @Override
    public Optional<Category> update(UpdateCategory updateCategory) {
        return categoryRepository.update(updateCategory)
                .map(categoryServiceMapper::categoryModelToCategory);
    }

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return categoryRepository.exists(categoryId);
    }

    @Override
    public boolean existsByPositionId(Long positionId) {
        return categoryServiceMapper.categoryModelsToCategories(categoryRepository.findAll())
                .stream().anyMatch(category -> category.positionId().equals(positionId));
    }
}
