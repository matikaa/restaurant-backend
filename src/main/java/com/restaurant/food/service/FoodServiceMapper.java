package com.restaurant.food.service;

import com.restaurant.food.repository.dto.FoodModel;
import com.restaurant.food.service.dto.Food;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FoodServiceMapper {

    FoodServiceMapper INSTANCE = Mappers.getMapper(FoodServiceMapper.class);

    Food foodModelToFood(FoodModel foodModel);

    List<Food> foodModelsToFoods(List<FoodModel> foodModels);

    FoodModel foodToFoodModel(Food food);
}
