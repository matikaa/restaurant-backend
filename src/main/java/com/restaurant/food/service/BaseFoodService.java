package com.restaurant.food.service;

import com.restaurant.food.controller.dto.FoodRequest;
import com.restaurant.food.repository.FoodRepository;
import com.restaurant.food.service.dto.Food;

import java.util.List;
import java.util.Optional;

public class BaseFoodService implements FoodService {

    private static final FoodServiceMapper foodServiceMapper = FoodServiceMapper.INSTANCE;
    private final FoodRepository foodRepository;

    public BaseFoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public Food insert(FoodRequest foodRequest, Long categoryId) {
        return foodServiceMapper.foodModelToFood(foodRepository.save(foodRequest, categoryId));
    }

    @Override
    public boolean existsByPositionId(Long categoryId, Long positionId) {
        return foodServiceMapper.foodModelsToFoods(foodRepository.getFoodByCategoryId(categoryId))
                .stream().anyMatch(food -> food.positionId().equals(positionId));
    }

    @Override
    public List<Food> getFoodByCategoryId(Long categoryId) {
        return foodServiceMapper.foodModelsToFoods(foodRepository.getFoodByCategoryId(categoryId));
    }

    @Override
    public Optional<Food> getFoodByNameAndPrice(String foodName, Double foodPrice) {
        return foodRepository.getFoodByFoodNameAndFoodPrice(foodName, foodPrice)
                .map(foodServiceMapper::foodModelToFood);
    }

    @Override
    public boolean existsByCategoryIdAndFoodId(Long categoryId, Long foodId) {
        return foodRepository.existsByCategoryIdAndFoodId(categoryId, foodId);
    }

    @Override
    public void deleteById(Long foodId) {
        foodRepository.deleteById(foodId);
    }

    @Override
    public Optional<Food> update(Food food) {
        return foodRepository.update(foodServiceMapper.foodToFoodModel(food))
                .map(foodServiceMapper::foodModelToFood);
    }

    @Override
    public Optional<Food> getFoodByCategoryIdAndFoodId(Long categoryId, Long foodId) {
        return foodRepository.getFoodByCategoryIdAndFoodId(categoryId, foodId)
                .map(foodServiceMapper::foodModelToFood);
    }

    @Override
    public void deleteByCategoryId(Long categoryId) {
        foodRepository.deleteByCategoryId(categoryId);
    }
}
