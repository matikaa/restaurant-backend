package com.restaurant.app.food.service;

import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.service.dto.Food;

import java.util.List;
import java.util.Optional;

public interface FoodService {

    Food insert(FoodRequest foodRequest, Long categoryId);

    boolean existsByPositionId(Long categoryId, Long positionId);

    List<Food> getFoodByCategoryId(Long categoryId);

    void deleteById(Long foodId);

    Optional<Food> update(Food food);

    Optional<Food> getFoodByCategoryIdAndFoodId(Long categoryId, Long foodId);

    boolean existsByCategoryIdAndFoodId(Long categoryId, Long foodId);
}
