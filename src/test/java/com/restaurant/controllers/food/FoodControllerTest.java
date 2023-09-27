package com.restaurant.controllers.food;

import com.restaurant.food.controller.dto.FoodListResponse;
import com.restaurant.food.controller.dto.FoodRequestResponse;
import com.restaurant.food.controller.dto.FoodResponse;
import com.restaurant.controllers.TestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

class FoodControllerTest extends TestUseCase {

    @Test
    @DisplayName("Should add food and return 200 OK")
    void shouldAddFoodAndReturnOk() {
        //given
        var categoryName = "Side Dishes";
        var categoryPositionId = 4L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Sauce";
        var foodPositionId = 2L;
        var foodPrice = 45D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));
        assertThat(foodResponse.getBody().foodName(), is(equalTo(foodName)));
        assertThat(foodResponse.getBody().foodPrice(), is(equalTo(foodPrice)));
        assertThat(foodResponse.getBody().positionId(), is(equalTo(foodPositionId)));
        assertThat(foodResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));
    }

    @Test
    @DisplayName("Should not add food and return 400 BAD REQUEST")
    void shouldNotAddAndReturnBadRequest() {
        //given
        var categoryName = "Drinks";
        var categoryPositionId = 4L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Mojito";
        var foodPositionId = 2L;
        var foodPrice = 45D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        var secondFoodName = "Sex on the beach";
        var secondFoodPrice = 38D;
        var invalidFoodRequest = createFoodRequest(foodPositionId, secondFoodName, secondFoodPrice);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));
        assertThat(foodResponse.getBody().foodName(), is(equalTo(foodName)));
        assertThat(foodResponse.getBody().foodPrice(), is(equalTo(foodPrice)));
        assertThat(foodResponse.getBody().positionId(), is(equalTo(foodPositionId)));
        assertThat(foodResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));

        //when
        foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                invalidFoodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(BAD_REQUEST)));
    }

    @Test
    @DisplayName("Should not add food by wrong categoryId and return 404 NOT FOUND")
    void shouldNotAddFoodByWrongCategoryIdAndReturnNotFound() {
        //given
        var foodName = "Grilled Steak";
        var foodPositionId = 1L;
        var foodPrice = 34D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        runAsAdmin();

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(WRONG_ID),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should get food by categoryId and foodId and return 200 OK")
    void shouldGetFoodAndReturnOk() {
        //given
        var categoryName = "Specials of the Day";
        var categoryPositionId = 4L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Roast Chicken";
        var foodPositionId = 2L;
        var foodPrice = 45D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));

        //when
        var foodRequestResponse = client.getForEntity(
                prepareFoodUrlWithFoodIdAndCategoryId(
                        savedCategory.categoryId(), foodResponse.getBody().foodId()),
                FoodRequestResponse.class
        );

        //then
        assertThat(foodRequestResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(foodRequestResponse.getBody(), is(notNullValue()));
        assertThat(foodRequestResponse.getBody().foodName(), is(equalTo(foodRequest.foodName())));
        assertThat(foodRequestResponse.getBody().foodPrice(), is(equalTo(foodRequest.foodPrice())));
        assertThat(foodRequestResponse.getBody().positionId(), is(equalTo(foodRequest.positionId())));
    }

    @Test
    @DisplayName("Should not get food by wrong categoryId and return 404 NOT FOUND")
    void shouldNotGetFoodByWrongCategoryIdAndReturnNotFound() {
        //given
        var categoryName = "Desserts";
        var categoryPositionId = 4L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Ice Cream";
        var foodPositionId = 2L;
        var foodPrice = 45D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));

        //when
        var foodRequestResponse = client.getForEntity(
                prepareFoodUrlWithFoodIdAndCategoryId(
                        WRONG_ID, foodResponse.getBody().foodId()),
                FoodRequestResponse.class
        );

        //then
        assertThat(foodRequestResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should not get food by wrong foodId and return 404 NOT FOUND")
    void shouldNotGetFoodByWrongFoodIdAndReturnNotFound() {
        //given
        var categoryName = "Entrees";
        var categoryPositionId = 4L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Caesar Salad";
        var foodPositionId = 2L;
        var foodPrice = 45D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));

        //when
        var foodRequestResponse = client.getForEntity(
                prepareFoodUrlWithFoodIdAndCategoryId(
                        WRONG_ID, foodResponse.getBody().foodId()),
                FoodRequestResponse.class
        );

        //then
        assertThat(foodRequestResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should get empty list of food by categoryId and return 200 OK")
    void shouldGetFoodByCategoryIdWithoutAddedFoodAndReturnOK() {
        //given
        var categoryName = "Kid's menu";
        var categoryPositionId = 4L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        //when
        var foodListResponse = client.getForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                FoodListResponse.class
        );

        //then
        assertThat(foodListResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(foodListResponse.getBody(), is(notNullValue()));
        assertThat(foodListResponse.getBody().foodResponses(), is(equalTo(emptyList())));
    }

    @Test
    @DisplayName("Should get all food by specific categoryId and return 200 OK")
    void shouldGetAllFoodBySpecificCategoryIdAndReturnOk() {
        //given
        var categoryName = "Fishes";
        var categoryPositionId = 4L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var categoryName2 = "Desserts";
        var categoryPositionId2 = 5L;
        var savedCategory2 = saveCategory(categoryName2, categoryPositionId2);

        var foodName = "Fillet";
        var foodPositionId = 1L;
        var foodPrice = 45D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        var foodName2 = "Tomato spaghetti";
        var foodPositionId2 = 2L;
        var foodPrice2 = 27D;
        var foodRequest2 = createFoodRequest(foodPositionId2, foodName2, foodPrice2);

        var foodName3 = "Lasagna";
        var foodPositionId3 = 1L;
        var foodPrice3 = 21D;
        var foodRequest3 = createFoodRequest(foodPositionId3, foodName3, foodPrice3);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));

        //when
        var foodResponse2 = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest2,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse2.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse2.getBody(), is(notNullValue()));

        //when
        var foodResponse3 = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory2.categoryId()),
                foodRequest3,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse3.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse3.getBody(), is(notNullValue()));

        //when
        var foodRequestResponse = client.getForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                FoodListResponse.class
        );

        //then
        assertThat(foodRequestResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(foodRequestResponse.getBody(), is(notNullValue()));
        assertThat(foodRequestResponse.getBody().foodResponses().get(0).categoryId(), is(savedCategory.categoryId()));
        assertThat(foodRequestResponse.getBody().foodResponses().get(0).positionId(), is(foodRequest.positionId()));
        assertThat(foodRequestResponse.getBody().foodResponses().get(0).foodName(), is(foodRequest.foodName()));
        assertThat(foodRequestResponse.getBody().foodResponses().get(0).foodPrice(), is(foodRequest.foodPrice()));

        assertThat(foodRequestResponse.getBody().foodResponses().get(1).categoryId(), is(savedCategory.categoryId()));
        assertThat(foodRequestResponse.getBody().foodResponses().get(1).positionId(), is(foodRequest2.positionId()));
        assertThat(foodRequestResponse.getBody().foodResponses().get(1).foodName(), is(foodRequest2.foodName()));
        assertThat(foodRequestResponse.getBody().foodResponses().get(1).foodPrice(), is(foodRequest2.foodPrice()));
    }

    @Test
    @DisplayName("Should delete food and return 404 NOT FOUND")
    void shouldDeleteFoodAndReturnNotFound() {
        //given
        var categoryName = "Soups";
        var categoryPositionId = 3L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Tomato soup";
        var foodPositionId = 2L;
        var foodPrice = 24D;
        var savedFood = saveFood(savedCategory.categoryId(), foodName, foodPrice, foodPositionId);

        //when
        client.delete(prepareFoodUrlWithFoodIdAndCategoryId(savedCategory.categoryId(), savedFood.foodId()));

        var contactDeleted = client.getForEntity(
                prepareFoodUrlWithFoodIdAndCategoryId(savedCategory.categoryId(), savedFood.foodId()),
                FoodRequestResponse.class
        );

        //then
        assertThat(contactDeleted.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should not delete food by wrong categoryId and get 200 OK")
    void shouldNotDeleteFoodByWrongCategoryIdAndReturnOK() {
        //given
        var categoryName = "Soups";
        var categoryPositionId = 3L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Tomato soup";
        var foodPositionId = 2L;
        var foodPrice = 24D;
        var savedFood = saveFood(savedCategory.categoryId(), foodName, foodPrice, foodPositionId);

        //when
        client.delete(prepareFoodUrlWithFoodIdAndCategoryId(WRONG_ID, savedFood.foodId()));

        var contactDeleted = client.getForEntity(
                prepareFoodUrlWithFoodIdAndCategoryId(savedCategory.categoryId(), savedFood.foodId()),
                FoodRequestResponse.class
        );

        //then
        assertThat(contactDeleted.getStatusCode(), is(equalTo(OK)));
        assertThat(contactDeleted.getBody(), is(notNullValue()));
    }

    @Test
    @DisplayName("Should not delete food by wrong foodId and get 200 OK")
    void shouldNotDeleteFoodByWrongFoodIdAndReturnOK() {
        //given
        var categoryName = "Soups";
        var categoryPositionId = 3L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Tomato soup";
        var foodPositionId = 2L;
        var foodPrice = 24D;
        var savedFood = saveFood(savedCategory.categoryId(), foodName, foodPrice, foodPositionId);

        var incorrectFoodId = -1L;

        //when
        client.delete(prepareFoodUrlWithFoodIdAndCategoryId(savedCategory.categoryId(), incorrectFoodId));

        var contactDeleted = client.getForEntity(
                prepareFoodUrlWithFoodIdAndCategoryId(savedCategory.categoryId(), savedFood.foodId()),
                FoodRequestResponse.class
        );

        //then
        assertThat(contactDeleted.getStatusCode(), is(equalTo(OK)));
        assertThat(contactDeleted.getBody(), is(notNullValue()));
    }

    @Test
    @DisplayName("Should update food return 200 OK")
    void shouldUpdateFoodAndReturnOk() {
        //given
        var categoryName = "Teas";
        var categoryPositionId = 6L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Earl grey";
        var foodPositionId = 1L;
        var foodPrice = 15D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        var secPositionId = 3L;
        var secFoodPrice = 63D;
        var updateFoodRequest = createFoodRequestUpdate(savedCategory.categoryId(), secPositionId, foodName, secFoodPrice);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));

        //when
        var updatedFoodResponse = client.exchange(
                prepareFoodUrlWithFoodIdAndCategoryId(savedCategory.categoryId(),
                        foodResponse.getBody().foodId()),
                PUT,
                createBody(updateFoodRequest),
                FoodResponse.class
        );

        //then
        assertThat(updatedFoodResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(updatedFoodResponse.getBody(), is(notNullValue()));
        assertThat(updatedFoodResponse.getBody().foodName(), is(equalTo(foodResponse.getBody().foodName())));
        assertThat(updatedFoodResponse.getBody().categoryId(), is(equalTo(foodResponse.getBody().categoryId())));
        assertThat(updatedFoodResponse.getBody().positionId(), is(equalTo(updateFoodRequest.positionId())));
        assertThat(updatedFoodResponse.getBody().foodPrice(), is(equalTo(updateFoodRequest.foodPrice())));

        //when
        var foodRequestResponse = client.getForEntity(
                prepareFoodUrlWithFoodIdAndCategoryId(
                        savedCategory.categoryId(),
                        foodResponse.getBody().foodId()),
                FoodRequestResponse.class
        );

        //then
        assertThat(foodRequestResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(foodRequestResponse.getBody(), is(notNullValue()));
        assertThat(foodRequestResponse.getBody().positionId(), is(equalTo(updateFoodRequest.positionId())));
        assertThat(foodRequestResponse.getBody().foodPrice(), is(equalTo(updateFoodRequest.foodPrice())));
    }

    @Test
    @DisplayName("Should not update food by wrong categoryId return 404 NOT FOUND")
    void shouldNotUpdateFoodByWrongCategoryIdAndReturnNotFound() {
        //given
        var categoryName = "Coffees";
        var categoryPositionId = 2L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Cappuccino";
        var foodPositionId = 5L;
        var foodPrice = 6D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        var secPositionId = 2L;
        var secFoodPrice = 15D;
        var updateFoodRequest = createFoodRequestUpdate(savedCategory.categoryId(), secPositionId, foodName, secFoodPrice);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));

        //when
        var updatedFoodResponse = client.exchange(
                prepareFoodUrlWithFoodIdAndCategoryId(WRONG_ID, foodResponse.getBody().foodId()),
                PUT,
                createBody(updateFoodRequest),
                FoodResponse.class
        );

        //then
        assertThat(updatedFoodResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should not update food by wrong foodId return 404 NOT FOUND")
    void shouldNotUpdateFoodByWrongFoodIdAndReturnNotFound() {
        //given
        var wrongFoodId = -1L;
        var categoryName = "Coffees";
        var categoryPositionId = 2L;
        var savedCategory = saveCategory(categoryName, categoryPositionId);

        var foodName = "Cappuccino";
        var foodPositionId = 5L;
        var foodPrice = 6D;
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        var secPositionId = 2L;
        var secFoodPrice = 15D;
        var updateFoodRequest = createFoodRequestUpdate(savedCategory.categoryId(), secPositionId, foodName, secFoodPrice);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(savedCategory.categoryId()),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));

        //when
        var updatedFoodResponse = client.exchange(
                prepareFoodUrlWithFoodIdAndCategoryId(savedCategory.categoryId(), wrongFoodId),
                PUT,
                createBody(updateFoodRequest),
                FoodResponse.class
        );

        //then
        assertThat(updatedFoodResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }
}
