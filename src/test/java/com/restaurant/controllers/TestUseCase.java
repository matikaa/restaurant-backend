package com.restaurant.controllers;

import com.restaurant.app.App;
import com.restaurant.app.category.controller.dto.CategoryRequest;
import com.restaurant.app.category.controller.dto.CategoryRequestResponse;
import com.restaurant.app.category.controller.dto.UpdateCategoryRequest;
import com.restaurant.app.contact.controller.dto.ContactRequest;
import com.restaurant.app.contact.controller.dto.ContactRequestResponse;
import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.controller.dto.FoodRequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestUseCase {

    @Autowired
    protected TestRestTemplate client;

    @LocalServerPort
    protected int port;

    protected static final Long WRONG_CATEGORY_ID = -1L;
    protected static final String CONTACT_URL = "/contact";
    protected static final Long POSITION_ID = 1L;
    protected static final String CATEGORY_RESOURCE = "/categories";
    protected static final String FOOD_URL = "/food";
    protected static final String FOOD_GET_URL = "/food/%d";
    protected static final String CATEGORY_PATH = CATEGORY_RESOURCE + "/%d";

    private static final String GET_CONTACT_URL = CONTACT_URL + "/%d";
    private static final String BASE_URL = "http://localhost:%d%s";

    protected CategoryRequestResponse saveCategory(String categoryName, Long positionId) {
        //given
        var createCategoryRequest = new CategoryRequest(positionId, categoryName);

        //when
        var createCategoryResponse = client.postForEntity(
                prepareUrl(CATEGORY_RESOURCE),
                createCategoryRequest,
                CategoryRequestResponse.class
        );

        //then
        assertThat(createCategoryResponse.getStatusCode(), equalTo(CREATED));
        assertThat(createCategoryResponse.getBody(), is(not(nullValue())));
        return createCategoryResponse.getBody();
    }

    protected FoodRequestResponse saveFood(Long categoryId, String foodName, Integer foodPrice, Long foodPositionId){
        var foodRequest = createFoodRequest(categoryId, foodPositionId, foodName, foodPrice);

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithoutFoodId(),
                foodRequest,
                FoodRequestResponse.class
        );

        //then
        assertThat(foodResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(foodResponse.getBody(), is(notNullValue()));
        return foodResponse.getBody();
    }

    protected UpdateCategoryRequest getUpdatedCategoryRequest(Long positionId, String categoryName) {
        return new UpdateCategoryRequest(positionId, categoryName);
    }

    protected String prepareUrl(String resource) {
        return String.format(BASE_URL, port, resource);
    }

    protected String prepareFoodUrlWithoutFoodId() {
        return prepareUrl(CATEGORY_RESOURCE + FOOD_URL);
    }

    protected String prepareFoodUrlWithCategoryId(Long categoryId) {
        return prepareUrl(String.format(CATEGORY_PATH, categoryId) + FOOD_URL);
    }

    protected String prepareFoodUrlWithFoodId(Long categoryId, Long foodId){
        return categoryPath(categoryId) + String.format(FOOD_GET_URL, foodId);
    }

    protected String categoryPath(Long categoryId) {
        return prepareUrl(String.format(CATEGORY_PATH, categoryId));
    }

    protected String prepareGetContactUrl(Long id) {
        return prepareUrl(String.format(GET_CONTACT_URL, id));
    }

    protected ContactRequest createContactRequest() {
        return new ContactRequest(
                "company@temp.pl",
                "509283543",
                "pon-sb",
                "9.00",
                "22.00",
                "Warszawa",
                "Zlota",
                43
        );
    }

    protected ContactRequestResponse saveContact() {
        //given
        var contactRequest = createContactRequest();

        //when
        var contactRequestResponse = client.postForEntity(
                prepareUrl(CONTACT_URL),
                createContactRequest(),
                ContactRequestResponse.class);

        //then
        assertThat(contactRequestResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(contactRequestResponse.getBody(), is(notNullValue()));
        return contactRequestResponse.getBody();
    }

    protected ContactRequest createUpdateContactRequest() {
        return new ContactRequest(
                "restaurant@temp.pl",
                "521789037",
                "monday-saturday",
                "11.00",
                "24.00",
                "Poznan",
                "Golden Street",
                21
        );
    }

    protected FoodRequest createFoodRequest(Long categoryId, Long foodPosition, String foodName, Integer foodPrice){
        return new FoodRequest(categoryId, foodPosition, foodName, foodPrice);
    }

    protected <T> HttpEntity<Object> createBody(T body) {
        return new HttpEntity<>(body);
    }
}
