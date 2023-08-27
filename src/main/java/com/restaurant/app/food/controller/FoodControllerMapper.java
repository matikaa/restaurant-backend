package com.restaurant.app.food.controller;

import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.controller.dto.FoodRequestResponse;
import com.restaurant.app.food.controller.dto.FoodResponse;
import com.restaurant.app.food.service.dto.Food;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FoodControllerMapper {

    FoodControllerMapper INSTANCE = Mappers.getMapper(FoodControllerMapper.class);

    FoodResponse foodToFoodResponse(Food food);

    FoodRequestResponse foodToFoodRequestResponse(Food food);

    List<FoodResponse> foodsToFoodResponses(List<Food> foods);

    Food foodRequestToFood(Long categoryId, Long foodId, FoodRequest foodRequest);
}
