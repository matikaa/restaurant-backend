package com.restaurant.app.food.service;

import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.service.dto.Food;

import java.util.List;
import java.util.Optional;

public interface FoodService {

    List<Food> getAll();

    Food insert(FoodRequest foodRequest, Long categoryId);

    boolean existsByPositionId(Long categoryId, Long positionId);

    List<Food> getFoodByCategoryId(Long categoryId);

    boolean existsByFoodId(Long foodId);

    void deleteById(Long foodId);

    Optional<Food> update(Food food);

    Optional<Food> getFoodByCategoryIdAndFoodId(Long categoryId, Long foodId);
}
