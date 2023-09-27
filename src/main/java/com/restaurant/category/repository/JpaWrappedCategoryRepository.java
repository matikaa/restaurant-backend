package com.restaurant.category.repository;

import com.restaurant.category.controller.dto.CategoryRequest;
import com.restaurant.category.controller.dto.UpdateCategory;
import com.restaurant.category.repository.entity.CategoryModel;

import java.util.List;
import java.util.Optional;

public class JpaWrappedCategoryRepository implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    private static final CategoryRepositoryMapper categoryRepositoryMapper = CategoryRepositoryMapper.INSTANCE;

    public JpaWrappedCategoryRepository(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public Optional<CategoryModel> findCategoryById(Long categoryId) {
        return categoryJpaRepository.findById(categoryId)
                .map(categoryRepositoryMapper::categoryEntityToCategoryModel);
    }

    @Override
    public Optional<CategoryModel> update(UpdateCategory updateCategory) {
        return categoryJpaRepository.findById(updateCategory.categoryId())
                .map(category -> updateCategoryName(updateCategory, category))
                .map(categoryJpaRepository::save)
                .map(categoryRepositoryMapper::categoryEntityToCategoryModel);
    }

    @Override
    public void delete(Long categoryId) {
        categoryJpaRepository.deleteById(categoryId);
    }

    @Override
    public CategoryModel save(CategoryRequest categoryRequest) {
        return categoryRepositoryMapper.categoryEntityToCategoryModel(
                categoryJpaRepository.save(categoryRepositoryMapper.categoryRequestToCategoryEntity(categoryRequest)));
    }

    @Override
    public List<CategoryModel> findAll() {
        return categoryRepositoryMapper.categoryEntityToCategoryModels(categoryJpaRepository.findAll());
    }

    @Override
    public boolean exists(Long categoryId) {
        return categoryJpaRepository.existsById(categoryId);
    }

    private CategoryEntity updateCategoryName(UpdateCategory updateCategory, CategoryEntity categoryEntity) {
        categoryEntity.setCategoryName(updateCategory.categoryName());
        categoryEntity.setPositionId(updateCategory.positionId());
        return categoryEntity;
    }

}
