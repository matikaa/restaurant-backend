package com.restaurant.food.controller;

import com.restaurant.food.controller.dto.FoodRequestResponse;
import com.restaurant.food.controller.dto.FoodRequestUpdate;
import com.restaurant.food.controller.dto.FoodResponse;
import com.restaurant.food.service.dto.Food;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FoodControllerMapper {

    FoodControllerMapper INSTANCE = Mappers.getMapper(FoodControllerMapper.class);

    FoodResponse foodToFoodResponse(Food food);

    FoodRequestResponse foodToFoodRequestResponse(Food food);

    List<FoodResponse> foodsToFoodResponses(List<Food> foods);

    Food foodRequestUpdateToFood(Long foodId, FoodRequestUpdate foodRequestUpdate);
}
