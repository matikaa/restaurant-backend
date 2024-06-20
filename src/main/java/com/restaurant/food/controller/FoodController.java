package com.restaurant.food.controller;

import com.restaurant.food.controller.dto.*;
import com.restaurant.category.service.CategoryService;
import com.restaurant.common.ConstantValues;
import com.restaurant.food.controller.validator.FoodValidator;
import com.restaurant.food.service.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/categories")
public class FoodController {

    private static final FoodControllerMapper foodControllerMapper = FoodControllerMapper.INSTANCE;

    private final FoodValidator foodValidator;

    private final FoodService foodService;

    private final CategoryService categoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodController.class);

    public FoodController(FoodService foodService, CategoryService categoryService) {
        this.foodValidator = new FoodValidator();
        this.foodService = foodService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}/food")
    public ResponseEntity<FoodListResponse> getFoodByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok().body(new FoodListResponse(
                foodControllerMapper.foodsToFoodResponses(
                        foodService.getFoodByCategoryId(categoryId).stream().toList())));
    }

    @PostMapping("/food")
    public ResponseEntity<FoodResponse> getFoodByFoodName(@RequestBody FoodNameAndPrice foodNameAndPrice) {
        return foodService.getFoodByNameAndPrice(foodNameAndPrice.foodName(), foodNameAndPrice.foodPrice())
                .map(foodControllerMapper::foodToFoodResponse)
                .map(food -> ResponseEntity.ok().body(food))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{categoryId}/food/{foodId}")
    public ResponseEntity<FoodResponse> getFoodByCategoryIdAndFoodId(@PathVariable Long categoryId,
                                                                     @PathVariable Long foodId) {
        if (!foodService.existsByCategoryIdAndFoodId(categoryId, foodId)) {
            LOGGER.warn(ConstantValues.FOOD_WITH_CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }
        return foodService.getFoodByCategoryIdAndFoodId(categoryId, foodId)
                .map(foodControllerMapper::foodToFoodResponse)
                .map(food -> ResponseEntity.ok().body(food))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{categoryId}/food")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<FoodRequestResponse> addFood(@PathVariable Long categoryId, @RequestBody FoodRequest foodRequest) {
        if (foodValidator.isFoodRequestNotValid(foodRequest)) {
            LOGGER.warn(ConstantValues.INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (!categoryService.existsByCategoryId(categoryId)) {
            LOGGER.warn(ConstantValues.CATEGORY_NOT_EXISTS_TO_ADD_FOOD);
            return ResponseEntity.notFound().build();
        }

        if (foodService.existsByPositionId(categoryId, foodRequest.positionId())) {
            LOGGER.warn(ConstantValues.FOOD_POSITION_EXISTS);
            return ResponseEntity.badRequest().build();
        }

        return Optional.ofNullable(foodRequest)
                .map(food -> foodService.insert(food, categoryId))
                .map(foodControllerMapper::foodToFoodRequestResponse)
                .map(ResponseEntity.status(CREATED)::body)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoryId}/food/{foodId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteFood(@PathVariable Long categoryId, @PathVariable Long foodId) {
        if (!foodService.existsByCategoryIdAndFoodId(categoryId, foodId)) {
            LOGGER.warn(ConstantValues.FOOD_WITH_CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        foodService.deleteById(foodId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}/food/{foodId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<FoodResponse> updateFood(
            @PathVariable Long categoryId, @PathVariable Long foodId, @RequestBody FoodRequestUpdate foodRequestUpdate) {
        if (foodValidator.isFoodRequestUpdateNotValid(foodRequestUpdate)) {
            LOGGER.warn(ConstantValues.INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (!foodService.existsByCategoryIdAndFoodId(categoryId, foodId)) {
            LOGGER.warn(ConstantValues.FOOD_WITH_CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        var food = foodService.getFoodByCategoryIdAndFoodId(categoryId, foodId);

        if (food.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (foodService.existsByPositionId(foodRequestUpdate.categoryId(), foodRequestUpdate.positionId()) &&
                !food.get().positionId().equals(foodRequestUpdate.positionId())) {
            LOGGER.warn(ConstantValues.FOOD_POSITION_EXISTS);
            return ResponseEntity.badRequest().build();
        }

        return foodService.update(foodControllerMapper.foodRequestUpdateToFood(foodId, foodRequestUpdate))
                .map(updatedFood -> ResponseEntity.ok().body(foodControllerMapper.foodToFoodResponse(updatedFood)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
