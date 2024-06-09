package com.restaurant.food.service;

import com.restaurant.food.controller.dto.FoodRequest;
import com.restaurant.food.service.dto.Food;

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

    Optional<Food> getFoodByNameAndPrice(String foodName, Double foodPrice);

    void deleteByCategoryId(Long categoryId);
}
