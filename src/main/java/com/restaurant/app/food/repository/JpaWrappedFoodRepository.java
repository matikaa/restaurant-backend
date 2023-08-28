package com.restaurant.app.food.repository;

import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.repository.dto.FoodModel;

import java.util.List;
import java.util.Optional;

public class JpaWrappedFoodRepository implements FoodRepository {

    private static final FoodRepositoryMapper foodRepositoryMapper = FoodRepositoryMapper.INSTANCE;

    private final FoodJpaRepository foodJpaRepository;

    public JpaWrappedFoodRepository(FoodJpaRepository foodJpaRepository) {
        this.foodJpaRepository = foodJpaRepository;
    }

    @Override
    public FoodModel save(FoodRequest foodRequest, Long categoryId) {
        return foodRepositoryMapper.foodEntityToFoodModel(
                foodJpaRepository.save(foodRepositoryMapper.foodRequestToFoodEntity(foodRequest, categoryId)));
    }

    @Override
    public List<FoodModel> getFoodByCategoryId(Long categoryId) {
        return foodRepositoryMapper.foodEntityToFoodModels(foodJpaRepository.getFoodByCategoryId(categoryId));
    }

    public boolean existsByCategoryIdAndFoodId(Long categoryId, Long foodId) {
        return foodJpaRepository.existsByCategoryIdAndFoodId(categoryId, foodId);
    }

    @Override
    public void deleteById(Long foodId) {
        foodJpaRepository.deleteById(foodId);
    }

    @Override
    public Optional<FoodModel> update(FoodModel foodModel) {
        return foodJpaRepository.findById(foodModel.foodId())
                .map(foodToUpdate -> foodRepositoryMapper.foodModelToFoodEntity(foodModel))
                .map(foodJpaRepository::save)
                .map(foodRepositoryMapper::foodEntityToFoodModel);
    }

    @Override
    public Optional<FoodModel> getFoodByCategoryIdAndFoodId(Long categoryId, Long foodId){
        return foodJpaRepository.getFoodByCategoryIdAndFoodId(categoryId, foodId)
                .map(foodRepositoryMapper::foodEntityToFoodModel);
    }
}
