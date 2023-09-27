package com.restaurant.category.service;

import com.restaurant.category.repository.entity.CategoryModel;
import com.restaurant.category.service.dto.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryServiceMapper {

    CategoryServiceMapper INSTANCE = Mappers.getMapper(CategoryServiceMapper.class);

    List<Category> categoryModelsToCategories(List<CategoryModel> categoryModels);

    Category categoryModelToCategory(CategoryModel categoryModel);
}
