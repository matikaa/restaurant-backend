package com.restaurant.food.repository;

import com.restaurant.food.controller.dto.FoodRequest;
import com.restaurant.food.repository.dto.FoodModel;

import java.util.List;
import java.util.Optional;

public interface FoodRepository {

    FoodModel save(FoodRequest foodRequest, Long categoryId);

    List<FoodModel> getFoodByCategoryId(Long categoryId);

    void deleteById(Long foodId);

    Optional<FoodModel> update(FoodModel foodModel);

    Optional<FoodModel> getFoodByCategoryIdAndFoodId(Long categoryId, Long foodId);

    boolean existsByCategoryIdAndFoodId(Long categoryId, Long foodId);
}
