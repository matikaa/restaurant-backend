package com.restaurant.app.food.service;

import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.repository.FoodRepository;
import com.restaurant.app.food.service.dto.Food;

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
    public List<Food> getAll() {
        return foodServiceMapper.foodModelsToFoods(foodRepository.findAll());
    }

    @Override
    public List<Food> getFoodByCategoryId(Long categoryId) {
        return foodServiceMapper.foodModelsToFoods(foodRepository.getFoodByCategoryId(categoryId));
    }

    @Override
    public boolean existsByFoodId(Long foodId) {
        return foodRepository.existsByFoodId(foodId);
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
    public Optional<Food> getFoodByCategoryIdAndFoodId(Long categoryId, Long foodId){
        return foodRepository.getFoodByCategoryIdAndFoodId(categoryId, foodId)
                .map(foodServiceMapper::foodModelToFood);
    }
}
