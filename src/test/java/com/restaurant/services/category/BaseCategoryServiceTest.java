package com.restaurant.services.category;

import com.restaurant.category.controller.dto.CategoryRequest;
import com.restaurant.category.controller.dto.UpdateCategory;
import com.restaurant.category.repository.CategoryRepository;
import com.restaurant.category.repository.entity.CategoryModel;
import com.restaurant.category.service.BaseCategoryService;
import com.restaurant.category.service.dto.Category;
import com.restaurant.services.BaseTestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseCategoryServiceTest extends BaseTestUseCase {

    @InjectMocks
    private BaseCategoryService baseCategoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should get all categories")
    void shouldGetAllCategories() {
        //given
        Long categoryId = 1L;
        Long positionId = 2L;
        String categoryName = "Starters";

        CategoryModel categoryModel = new CategoryModel(categoryId, positionId, categoryName);
        List<CategoryModel> categoryModels = new ArrayList<>();
        categoryModels.add(categoryModel);

        Category category = new Category(categoryId, positionId, categoryName);
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryRepository.findAll()).thenReturn(categoryModels);

        //when
        List<Category> resultOfCategories = baseCategoryService.getAll();

        //then
        assertEquals(categories, resultOfCategories);
    }

    @Test
    @DisplayName("Should insert and return category")
    void shouldInsertCategory() {
        //given
        Long categoryId = 1L;
        Long positionId = 5L;
        String categoryName = "Desserts";

        CategoryRequest categoryRequest = new CategoryRequest(positionId, categoryName);
        CategoryModel categoryModel = new CategoryModel(categoryId, positionId, categoryName);
        Category category = new Category(categoryId, positionId, categoryName);

        when(categoryRepository.save(categoryRequest)).thenReturn(categoryModel);

        //when
        Category savedCategory = baseCategoryService.insert(categoryRequest);

        //then
        assertEquals(category, savedCategory);
    }

    @Test
    @DisplayName("Should find category by Id")
    void shouldFindCategoryById() {
        //given
        Long categoryId = 1L;
        Long positionId = 5L;
        String categoryName = "Soups";

        Optional<CategoryModel> categoryModel = Optional.of(new CategoryModel(categoryId, positionId, categoryName));
        Optional<Category> category = Optional.of(new Category(categoryId, positionId, categoryName));

        when(categoryRepository.findCategoryById(categoryId)).thenReturn(categoryModel);

        //when
        Optional<Category> categoryResult = baseCategoryService.findById(categoryId);

        //then
        assertEquals(category, categoryResult);
    }

    @Test
    @DisplayName("Should update and return category")
    void shouldUpdateCategory() {
        //given
        Long categoryId = 5L;
        Long positionId = 3L;
        String categoryName = "Meats";

        UpdateCategory updateCategory = new UpdateCategory(categoryId, positionId, categoryName);
        Optional<CategoryModel> categoryModel = Optional.of(new CategoryModel(categoryId, positionId, categoryName));
        Optional<Category> category = Optional.of(new Category(categoryId, positionId, categoryName));

        when(categoryRepository.update(updateCategory)).thenReturn(categoryModel);

        //when
        Optional<Category> updateResult = baseCategoryService.update(updateCategory);

        //then
        assertEquals(category, updateResult);
    }

    @Test
    @DisplayName("Should check if category exists by categoryId")
    void shouldCheckIfCategoryExistsByCategoryId() {
        //given
        boolean existsCategory = true;
        Long categoryId = 4L;

        when(categoryRepository.exists(categoryId)).thenReturn(existsCategory);

        //when
        boolean existsResult = baseCategoryService.existsByCategoryId(categoryId);

        //then
        assertEquals(existsCategory, existsResult);
    }

    @Test
    @DisplayName("Should check if category exists by positionId")
    void shouldCheckIfCategoryExistsByPositionId() {
        //given
        boolean existsCategory = true;
        Long categoryId = 2L;
        Long positionId = 16L;
        String categoryName = "Kids menu";

        CategoryModel categoryModel = new CategoryModel(categoryId, positionId, categoryName);
        List<CategoryModel> categoryModels = new ArrayList<>();
        categoryModels.add(categoryModel);

        when(categoryRepository.findAll()).thenReturn(categoryModels);

        //when
        boolean existsResult = baseCategoryService.existsByPositionId(positionId);

        //then
        assertEquals(existsCategory, existsResult);
    }
}
