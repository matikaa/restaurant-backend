package com.restaurant.app.food.repository;

import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.repository.dto.FoodModel;

import java.util.List;
import java.util.Optional;

public interface FoodRepository {

    FoodModel save(FoodRequest foodRequest, Long categoryId);

    List<FoodModel> findAll();

    List<FoodModel> getFoodByCategoryId(Long categoryId);

    boolean existsByFoodId(Long foodId);

    void deleteById(Long foodId);

    Optional<FoodModel> update(FoodModel foodModel);

    Optional<FoodModel> getFoodByCategoryIdAndFoodId(Long categoryId, Long foodId);
}
