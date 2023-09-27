package com.restaurant.food.repository;

import com.restaurant.food.controller.dto.FoodRequest;
import com.restaurant.food.repository.dto.FoodModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FoodRepositoryMapper {

    FoodRepositoryMapper INSTANCE = Mappers.getMapper(FoodRepositoryMapper.class);

    FoodEntity foodRequestToFoodEntity(FoodRequest foodRequest, Long categoryId);

    FoodModel foodEntityToFoodModel(FoodEntity foodEntity);

    List<FoodModel> foodEntityToFoodModels(List<FoodEntity> foodEntities);

    FoodEntity foodModelToFoodEntity(FoodModel foodModel);
}
