package com.restaurant.app.food.controller.validator;

import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.controller.dto.FoodRequestUpdate;

public class FoodValidator {

    public FoodValidator() {
    }

    public boolean isFoodRequestNotValid(FoodRequest foodRequest) {
        return !(foodRequest instanceof FoodRequest) || foodRequest.positionId() == null ||
                foodRequest.positionId() <= 0 || foodRequest.foodName() == null ||
                foodRequest.foodName().trim().isEmpty() || foodRequest.foodPrice() == null ||
                foodRequest.foodPrice() <= 0;
    }

    public boolean isFoodRequestUpdateNotValid(FoodRequestUpdate foodRequestUpdate) {
        return !(foodRequestUpdate instanceof FoodRequestUpdate) ||
                foodRequestUpdate.categoryId() == null || foodRequestUpdate.categoryId() <= 0 ||
                foodRequestUpdate.positionId() == null || foodRequestUpdate.positionId() <= 0 ||
                foodRequestUpdate.foodName() == null || foodRequestUpdate.foodName().trim().isEmpty() ||
                foodRequestUpdate.foodPrice() == null || foodRequestUpdate.foodPrice() <= 0;
    }
}
