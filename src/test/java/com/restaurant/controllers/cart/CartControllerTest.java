package com.restaurant.controllers.cart;

import com.restaurant.app.cart.controller.dto.CartDeliveredResponse;
import com.restaurant.app.cart.controller.dto.CartResponse;
import com.restaurant.controllers.TestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

class CartControllerTest extends TestUseCase {

    @Test
    @DisplayName("Should add to order with discount and return 200 OK")
    void shouldAddToOrderAndReturnOk() {
        //given
        var foodPrice = 30D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsAdmin();
        //when
        var orderResponse = client.exchange(
                prepareOrderUrlWithFoodIdAndCategoryId(savedFood.categoryId(), savedFood.foodId()),
                PUT,
                null,
                CartResponse.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(orderResponse.getBody(), is(notNullValue()));
        assertThat(orderResponse.getBody().cartValue(), is(equalTo(valueWithDiscount(foodPrice))));
        assertThat(orderResponse.getBody().foodPrice().get(0), is(equalTo(foodPrice)));
        assertThat(orderResponse.getBody().food().get(0), is(equalTo(FOOD_NAME)));
    }

    @Test
    @DisplayName("Should not add to order by wrong foodId and return 404 NOT FOUND")
    void shouldNotAddToOrderByWrongFoodIdAndReturnNotFound() {
        //given
        var foodPrice = 20D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsAdmin();
        //when
        var orderResponse = client.exchange(
                prepareOrderUrlWithFoodIdAndCategoryId(savedFood.categoryId(), WRONG_ID),
                PUT,
                null,
                CartResponse.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should not add to order by not enough money and return 402 PAYMENT REQUIRED")
    void shouldNotAddToOrderByNotEnoughMoneyAndReturnPaymentRequired() {
        //given
        var foodPrice = 40D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        //when
        var orderResponse = client.exchange(
                prepareOrderUrlWithFoodIdAndCategoryId(savedFood.categoryId(), savedFood.foodId()),
                PUT,
                null,
                CartResponse.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(PAYMENT_REQUIRED)));
    }

    @Test
    @DisplayName("Should not add to order by wrong status and return 409 CONFLICT")
    void shouldNotAddToOrderByWrongStatusAndReturnConflict() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        saveOrder(savedFood.categoryId(), savedFood.foodId());

        //when
        var orderResponse = client.exchange(
                prepareCartUrlWithUserId(USER_ID),
                PUT,
                null,
                CartResponse.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(orderResponse.getBody(), is(notNullValue()));
        assertThat(orderResponse.getBody().foodPrice().get(0), is(equalTo(foodPrice)));
        assertThat(orderResponse.getBody().cartValue(), is(equalTo(valueWithDelivery(foodPrice))));

        //when
        var addFoodResponse = client.exchange(
                prepareOrderUrlWithFoodIdAndCategoryId(savedFood.categoryId(), savedFood.foodId()),
                PUT,
                null,
                CartResponse.class
        );

        //then
        assertThat(addFoodResponse.getStatusCode(), is(equalTo(CONFLICT)));
    }

    @Test
    @DisplayName("Should delete from order and return 200 OK")
    void shouldDeleteFromOrderAndReturnOk() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        saveOrder(savedFood.categoryId(), savedFood.foodId());

        //when
        var orderResponse = client.postForEntity(
                prepareOrderUrlWithFoodIdAndCategoryId(savedFood.categoryId(), savedFood.foodId()),
                null,
                Void.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(OK)));
    }

    @Test
    @DisplayName("Should not delete from order by wrong foodId and return 404 NOT FOUND")
    void shouldNotDeleteFromOrderByWrongFoodIdAndReturnNotFound() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();

        //when
        var orderResponse = client.postForEntity(
                prepareOrderUrlWithFoodIdAndCategoryId(savedFood.categoryId(), WRONG_ID),
                null,
                Void.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should not delete from order by not existing food and return 400 BAD REQUEST")
    void shouldNotDeleteFromOrderByNotExistingFoodAndReturnBadRequest() {
        //given
        var foodPrice = 5D;
        var positionId = 2L;

        var createdCategory = saveCategory("coctails", positionId);
        var createdFood = saveFood(createdCategory.categoryId(), "steak", foodPrice, positionId);

        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        saveOrder(savedFood.categoryId(), savedFood.foodId());

        //when
        var orderResponse = client.postForEntity(
                prepareOrderUrlWithFoodIdAndCategoryId(createdFood.categoryId(), createdFood.foodId()),
                null,
                Void.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(BAD_REQUEST)));
    }

    @Test
    @DisplayName("Should not delete from order by empty cart and return 409 CONFLICT")
    void shouldNotDeleteFromOrderByEmptyCartAndReturnConflict() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();

        //when
        var orderResponse = client.postForEntity(
                prepareOrderUrlWithFoodIdAndCategoryId(savedFood.categoryId(), savedFood.foodId()),
                null,
                Void.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(CONFLICT)));
    }

    @Test
    @DisplayName("Should confirm order and return 200 OK")
    void shouldConfirmOrderAndReturnOK() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        saveOrder(savedFood.categoryId(), savedFood.foodId());

        //when
        var orderResponse = client.exchange(
                prepareCartUrlWithUserId(USER_ID),
                PUT,
                null,
                CartResponse.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(orderResponse.getBody(), is(notNullValue()));
        assertThat(orderResponse.getBody().foodPrice().get(0), is(equalTo(foodPrice)));
        assertThat(orderResponse.getBody().cartValue(), is(equalTo(valueWithDelivery(foodPrice))));
    }

    @Test
    @DisplayName("Should not confirm order by empty cart and return 404 NOT FOUND")
    void shouldNotConfirmOrderByEmptyCartAndReturnNotFound() {
        //given
        runAsUser();

        //when
        var orderResponse = client.exchange(
                prepareCartUrlWithUserId(USER_ID),
                PUT,
                null,
                Void.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should confirm delivery and return 200 OK")
    void shouldConfirmDeliveryAndReturnOK() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        saveOrder(savedFood.categoryId(), savedFood.foodId());

        //when
        var orderResponse = client.exchange(
                prepareCartUrlWithUserId(USER_ID),
                PUT,
                null,
                CartResponse.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(orderResponse.getBody(), is(notNullValue()));
        assertThat(orderResponse.getBody().foodPrice().get(0), is(equalTo(foodPrice)));
        assertThat(orderResponse.getBody().cartValue(), is(equalTo(valueWithDelivery(foodPrice))));

        //when
        var deliveryResponse = client.postForEntity(
                prepareCartUrlWithUserId(USER_ID),
                null,
                CartResponse.class
        );

        //then
        assertThat(deliveryResponse.getStatusCode(), is(equalTo(OK)));
    }

    @Test
    @DisplayName("Should not confirm delivery by wrong status and return 409 CONFLICT")
    void shouldNotConfirmDeliveryByWrongStatusAndReturnConflict() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        saveOrder(savedFood.categoryId(), savedFood.foodId());

        //when
        var deliveryResponse = client.postForEntity(
                prepareCartUrlWithUserId(USER_ID),
                null,
                CartResponse.class
        );

        //then
        assertThat(deliveryResponse.getStatusCode(), is(equalTo(CONFLICT)));
    }

    @Test
    @DisplayName("Should get current order and return 200 OK")
    void shouldGetCurrentOrderAndReturnOK() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        saveOrder(savedFood.categoryId(), savedFood.foodId());

        //when
        var currentOrderResponse = client.getForEntity(
                prepareCartUrlWithUserId(USER_ID),
                CartResponse.class
        );

        //then
        assertThat(currentOrderResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(currentOrderResponse.getBody(), is(notNullValue()));
        assertThat(currentOrderResponse.getBody().foodPrice().get(0), is(equalTo(foodPrice)));
        assertThat(currentOrderResponse.getBody().cartValue(), is(equalTo(valueWithDelivery(foodPrice))));
    }

    @Test
    @DisplayName("Should not get current order by empty cart and return 409 CONFLICT")
    void shouldNotGetCurrentOrderByEmptyCartAndReturnConflict() {
        //given
        runAsUser();

        //when
        var currentOrderResponse = client.getForEntity(
                prepareCartUrlWithUserId(USER_ID),
                CartResponse.class
        );

        //then
        assertThat(currentOrderResponse.getStatusCode(), is(equalTo(CONFLICT)));
    }

    @Test
    @DisplayName("Should cancel current order and return 200 OK")
    void shouldCancelCurrentOrderAndReturnOK() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        saveOrder(savedFood.categoryId(), savedFood.foodId());

        //when
        var currentOrderResponse = client.getForEntity(
                prepareCartUrlWithUserId(USER_ID),
                CartResponse.class
        );

        //then
        assertThat(currentOrderResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(currentOrderResponse.getBody(), is(notNullValue()));
        assertThat(currentOrderResponse.getBody().foodPrice().get(0), is(equalTo(foodPrice)));
        assertThat(currentOrderResponse.getBody().cartValue(), is(equalTo(valueWithDelivery(foodPrice))));

        //when
        var cancelOrderResponse = client.exchange(
                prepareCartUrlWithUserId(USER_ID),
                DELETE,
                null,
                Void.class
        );

        //then
        assertThat(cancelOrderResponse.getStatusCode(), is(equalTo(OK)));

        //when
        currentOrderResponse = client.getForEntity(
                prepareCartUrlWithUserId(USER_ID),
                CartResponse.class
        );

        //then
        assertThat(currentOrderResponse.getStatusCode(), is(equalTo(CONFLICT)));
    }

    @Test
    @DisplayName("Should not cancel current order by lack of order and return 409 CONFLICT")
    void shouldNotCancelCurrentOrderByLackOfOrderAndReturnConflict() {
        //given
        runAsUser();

        //when
        client.delete(prepareCartUrlWithUserId(USER_ID));

        var cancelOrderResponse = client.exchange(
                prepareCartUrlWithUserId(USER_ID),
                DELETE,
                null,
                Void.class
        );

        //then
        assertThat(cancelOrderResponse.getStatusCode(), is(equalTo(CONFLICT)));
    }

    @Test
    @DisplayName("Should get all user orders and return 200 OK")
    void shouldGetAllUserOrdersAndReturnOK() {
        //given
        var foodPrice = 5D;
        var savedFood = saveCategoryAndFood(foodPrice);

        runAsUser();
        saveOrder(savedFood.categoryId(), savedFood.foodId());

        //when
        var orderResponse = client.exchange(
                prepareCartUrlWithUserId(USER_ID),
                PUT,
                null,
                CartResponse.class
        );

        //then
        assertThat(orderResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(orderResponse.getBody(), is(notNullValue()));
        assertThat(orderResponse.getBody().foodPrice().get(0), is(equalTo(foodPrice)));
        assertThat(orderResponse.getBody().cartValue(), is(equalTo(valueWithDelivery(foodPrice))));

        //when
        var deliveryResponse = client.postForEntity(
                prepareCartUrlWithUserId(USER_ID),
                null,
                CartResponse.class
        );

        //then
        assertThat(deliveryResponse.getStatusCode(), is(equalTo(OK)));

        //when
        var allOrdersResponse = client.getForEntity(
                prepareOrderUrlWithUserId(USER_ID),
                CartDeliveredResponse.class
        );

        //then
        assertThat(allOrdersResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(allOrdersResponse.getBody(), is(notNullValue()));
        assertThat(allOrdersResponse.getBody().overallCartValue(), is(equalTo(valueWithDelivery(foodPrice))));
        assertThat(allOrdersResponse.getBody().cartResponses().get(0).foodPrice().get(0), is(equalTo(foodPrice)));
        assertThat(allOrdersResponse.getBody().cartResponses().get(0).cartValue(), is(equalTo(valueWithDelivery(foodPrice))));
    }
}
