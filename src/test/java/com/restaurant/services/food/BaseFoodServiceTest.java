package com.restaurant.services.food;

import com.restaurant.food.controller.dto.FoodRequest;
import com.restaurant.food.repository.FoodRepository;
import com.restaurant.food.repository.dto.FoodModel;
import com.restaurant.food.service.BaseFoodService;
import com.restaurant.food.service.dto.Food;
import com.restaurant.services.BaseTestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseFoodServiceTest extends BaseTestUseCase {

    @InjectMocks
    private BaseFoodService baseFoodService;

    @Mock
    private FoodRepository foodRepository;

    @Test
    @DisplayName("Should insert and return food")
    void shouldInsertFood() {
        //given
        Long categoryId = 1L;
        FoodRequest foodRequest = getFoodRequest();
        Food food = getFood();
        FoodModel foodModel = getFoodModel();

        when(foodRepository.save(foodRequest, categoryId)).thenReturn(foodModel);

        //when
        Food foodResult = baseFoodService.insert(foodRequest, categoryId);

        //then
        assertEquals(food, foodResult);
    }

    @Test
    @DisplayName("Should return true when exists by positionId")
    void shouldReturnTrueWhenExistsByPositionId() {
        //given
        Long categoryId = 1L;
        Long positionId = 1L;
        boolean existsByPositionId = true;
        FoodModel foodModel = getFoodModel();
        List<FoodModel> foodModels = new ArrayList<>();
        foodModels.add(foodModel);

        when(foodRepository.getFoodByCategoryId(categoryId)).thenReturn(foodModels);

        //when
        boolean existsResult = baseFoodService.existsByPositionId(categoryId, positionId);

        //then
        assertEquals(existsByPositionId, existsResult);
    }

    @Test
    @DisplayName("Should return food list by categoryId")
    void shouldReturnFoodListByCategoryId() {
        //given
        Long categoryId = 1L;

        FoodModel foodModel = getFoodModel();
        List<FoodModel> foodModels = new ArrayList<>();
        foodModels.add(foodModel);

        Food food = getFood();
        List<Food> foods = new ArrayList<>();
        foods.add(food);

        when(foodRepository.getFoodByCategoryId(categoryId)).thenReturn(foodModels);

        //when
        List<Food> foodsResult = baseFoodService.getFoodByCategoryId(categoryId);

        //then
        assertEquals(foods, foodsResult);
    }

    @Test
    @DisplayName("Should return true when exists by categoryId and FoodId")
    void shouldReturnTrueWhenExistsByCategoryIdAndFoodId() {
        //given
        Long categoryId = 1L;
        Long foodId = 1L;
        boolean foodExists = true;

        when(foodRepository.existsByCategoryIdAndFoodId(categoryId, foodId)).thenReturn(foodExists);

        //when
        boolean foodExistsResult = baseFoodService.existsByCategoryIdAndFoodId(categoryId, foodId);

        //then
        assertEquals(foodExists, foodExistsResult);
    }

    @Test
    @DisplayName("Should update and return food")
    void shouldUpdateFood() {
        //given
        FoodModel foodModelToUpdate = getFoodModelToUpdate();
        Optional<FoodModel> foodModelUpdated = Optional.of(getFoodModel());
        Optional<Food> foodUpdated = Optional.of(getFood());
        Food foodToUpdate = getFoodToUpdate();

        when(foodRepository.update(foodModelToUpdate)).thenReturn(foodModelUpdated);

        //when
        Optional<Food> foodUpdateResult = baseFoodService.update(foodToUpdate);

        //then
        assertEquals(foodUpdated, foodUpdateResult);
    }

    @Test
    @DisplayName("Should get food by categoryId and foodId")
    void shouldGetFoodByCategoryIdAndFoodId() {
        //given
        Long categoryId = 1L;
        Long foodId = 1L;
        Optional<FoodModel> foodModel = Optional.of(getFoodModel());
        Optional<Food> food = Optional.of(getFood());

        when(foodRepository.getFoodByCategoryIdAndFoodId(categoryId, foodId)).thenReturn(foodModel);

        //when
        Optional<Food> foodResult = baseFoodService.getFoodByCategoryIdAndFoodId(categoryId, foodId);

        //then
        assertEquals(food, foodResult);
    }
}
