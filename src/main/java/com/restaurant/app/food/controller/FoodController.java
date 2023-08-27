package com.restaurant.app.food.controller;

import com.restaurant.app.category.service.CategoryService;
import com.restaurant.app.food.controller.dto.FoodListResponse;
import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.controller.dto.FoodRequestResponse;
import com.restaurant.app.food.controller.dto.FoodResponse;
import com.restaurant.app.food.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/categories")
public class FoodController {

    private final FoodService foodService;

    private final CategoryService categoryService;

    private static final FoodControllerMapper foodControllerMapper = FoodControllerMapper.INSTANCE;

    public FoodController(FoodService foodService, CategoryService categoryService) {
        this.foodService = foodService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}/food")
    public ResponseEntity<FoodListResponse> getFoodByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok().body(new FoodListResponse(
                foodControllerMapper.foodsToFoodResponses(
                        foodService.getFoodByCategoryId(categoryId).stream().toList()
                )
        ));
    }

    @GetMapping("/{categoryId}/food/{foodId}")
    public ResponseEntity<FoodResponse> getFoodByCategoryIdAndFoodId(@PathVariable Long categoryId,
                                                                            @PathVariable Long foodId) {
        return foodService.getFoodByCategoryIdAndFoodId(categoryId, foodId)
                .map(foodControllerMapper::foodToFoodResponse)
                .map(food -> ResponseEntity.ok().body(food))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/food")
    public ResponseEntity<FoodRequestResponse> addFood(@RequestBody FoodRequest foodRequest) {
        if (!categoryService.existsByCategoryId(foodRequest.categoryId())) {
            return ResponseEntity.notFound().build();
        }

        if (foodService.existsByPositionId(foodRequest.categoryId(), foodRequest.positionId())) {
            return ResponseEntity.badRequest().build();
        }

        return Optional.ofNullable(foodRequest)
                .map(food -> foodService.insert(food, foodRequest.categoryId()))
                .map(foodControllerMapper::foodToFoodRequestResponse)
                .map(ResponseEntity.status(CREATED)::body)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoryId}/food/{foodId}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long categoryId, @PathVariable Long foodId) {
        if (!foodService.existsByFoodId(foodId)) {
            return ResponseEntity.notFound().build();
        }

        foodService.deleteById(foodId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}/food/{foodId}")
    public ResponseEntity<FoodResponse> updateFood(
            @PathVariable Long categoryId, @PathVariable Long foodId, @RequestBody FoodRequest foodRequest) {
        if (!categoryService.existsByCategoryId(categoryId)) {
            return ResponseEntity.notFound().build();
        }

        var food = foodService.getFoodByCategoryIdAndFoodId(categoryId, foodId);

        if (food.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (foodService.existsByPositionId(foodRequest.categoryId(), foodRequest.positionId()) && !food.get().positionId().equals(foodRequest.positionId())) {
            return ResponseEntity.badRequest().build();
        }

        return foodService.update(foodControllerMapper.foodRequestToFood(foodRequest.categoryId(), foodId, foodRequest))
                .map(updatedFood -> ResponseEntity.ok().body(foodControllerMapper.foodToFoodResponse(updatedFood)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}