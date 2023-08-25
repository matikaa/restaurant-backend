package com.restaurant.app.category.service;

import com.restaurant.app.category.service.dto.Category;
import com.restaurant.app.category.repository.entity.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryServiceMapper {

    CategoryServiceMapper INSTANCE = Mappers.getMapper(CategoryServiceMapper.class);

    List<Category> categoryModelsToCategories(List<CategoryModel> categoryModels);

    Category categoryModelToCategory(CategoryModel categoryModel);
}
