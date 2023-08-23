package com.restaurant.app.category.controller;

import com.restaurant.app.category.controller.dto.*;
import com.restaurant.app.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    private final static CategoryControllerMapper categoryControllerMapper = CategoryControllerMapper.INSTANCE;

    public CategoryController(CategoryService categoryService) {
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
        return categoryService.findById(categoryId)
                .map(category -> ResponseEntity.ok().body(
                        categoryControllerMapper.categoryToCategoryResponse(category)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryRequestResponse> addCategory(@RequestBody CategoryRequest categoryRequest) {
        return Optional.ofNullable(categoryRequest)
                .map(categoryService::insert)
                .map(categoryControllerMapper::categoryToCategoryRequestResponse)
                .map(ResponseEntity.status(CREATED)::body)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
        if(!categoryService.existsById(categoryId)) {
            return ResponseEntity.notFound().build();
        }

        categoryService.delete(categoryId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long categoryId,
                                                   @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        return categoryService.update(categoryControllerMapper.
                updateCategoryRequestToUpdateCategory(updateCategoryRequest, categoryId))
                .map(updatedCategory -> ResponseEntity.ok().body(
                        categoryControllerMapper.categoryToCategoryResponse(updatedCategory)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
