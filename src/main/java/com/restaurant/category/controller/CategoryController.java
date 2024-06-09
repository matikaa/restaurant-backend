package com.restaurant.category.controller;

import com.restaurant.category.controller.dto.*;
import com.restaurant.category.controller.validator.CategoryValidator;
import com.restaurant.category.service.CategoryService;
import com.restaurant.food.service.FoodService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.restaurant.common.ConstantValues.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final CategoryControllerMapper categoryControllerMapper = CategoryControllerMapper.INSTANCE;

    private final CategoryValidator categoryValidator;

    private final CategoryService categoryService;

    private final FoodService foodService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    public CategoryController(CategoryService categoryService, FoodService foodService) {
        this.foodService = foodService;
        this.categoryValidator = new CategoryValidator();
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<CategoryListResponse> getCategories() {
        return ResponseEntity.ok().body(new CategoryListResponse(
                categoryControllerMapper.categoriesToCategoryResponses(
                        categoryService.getAll()).stream().toList()));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        if (!categoryService.existsByCategoryId(categoryId)) {
            LOGGER.warn(CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return categoryService.findById(categoryId)
                .map(category -> ResponseEntity.ok().body(
                        categoryControllerMapper.categoryToCategoryResponse(category)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryRequestResponse> addCategory(@RequestBody CategoryRequest categoryRequest) {
        if (categoryValidator.isCategoryRequestNotValid(categoryRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (categoryService.existsByPositionId(categoryRequest.positionId())) {
            LOGGER.warn(CATEGORY_POSITION_EXISTS);
            return ResponseEntity.badRequest().build();
        }

        return Optional.ofNullable(categoryRequest)
                .map(categoryService::insert)
                .map(categoryControllerMapper::categoryToCategoryRequestResponse)
                .map(ResponseEntity.status(CREATED)::body)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
        if (!categoryService.existsByCategoryId(categoryId)) {
            LOGGER.warn(CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        foodService.deleteByCategoryId(categoryId);
        categoryService.delete(categoryId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public ResponseEntity<CategoryResponse> update(@PathVariable Long categoryId,
                                                   @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        if (categoryValidator.isUpdateCategoryRequestNotValid(updateCategoryRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (!categoryService.existsByCategoryId(categoryId)) {
            LOGGER.warn(CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return categoryService.update(categoryControllerMapper.
                        updateCategoryRequestToUpdateCategory(updateCategoryRequest, categoryId))
                .map(updatedCategory -> ResponseEntity.ok().body(
                        categoryControllerMapper.categoryToCategoryResponse(updatedCategory)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
