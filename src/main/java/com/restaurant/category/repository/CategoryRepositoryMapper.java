package com.restaurant.category.repository;

import com.restaurant.category.controller.dto.CategoryRequest;
import com.restaurant.category.repository.entity.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryRepositoryMapper {

    CategoryRepositoryMapper INSTANCE = Mappers.getMapper(CategoryRepositoryMapper.class);

    CategoryEntity categoryRequestToCategoryEntity(CategoryRequest categoryRequest);

    CategoryModel categoryEntityToCategoryModel(CategoryEntity categoryEntity);

    List<CategoryModel> categoryEntityToCategoryModels(List<CategoryEntity> categoryEntities);
}
