package com.restaurant.controllers.category;

import com.restaurant.app.category.controller.dto.CategoryListResponse;
import com.restaurant.app.category.controller.dto.CategoryRequest;
import com.restaurant.app.category.controller.dto.CategoryRequestResponse;
import com.restaurant.app.category.controller.dto.CategoryResponse;
import com.restaurant.controllers.TestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

class CategoryControllerTestUseCase extends TestUseCase {

    @Test
    @DisplayName("Should get categories and return 200 OK")
    void shouldGetCategories() {
        //given
        var categoryName = "Juices";
        var secCategoryName = "Soups";
        var secPositionId = 13L;
        var savedCategory = saveCategory(categoryName, POSITION_ID);
        var secSavedCategory = saveCategory(secCategoryName, secPositionId);

        //when
        var getCategoryResponse = client.getForEntity(
                prepareUrl(CATEGORY_RESOURCE),
                CategoryListResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(getCategoryResponse.getBody(), is(not(nullValue())));
        assertThat(getCategoryResponse.getBody().categoryResponses().size(), is(equalTo(2)));
        assertThat(getCategoryResponse.getBody().categoryResponses().get(0).categoryName(), is(equalTo(categoryName)));
        assertThat(getCategoryResponse.getBody().categoryResponses().get(0).positionId(), is(equalTo(POSITION_ID)));
        assertThat(getCategoryResponse.getBody().categoryResponses().get(0).categoryId(), is(equalTo(savedCategory.categoryId())));

        assertThat(getCategoryResponse.getBody().categoryResponses().get(1).categoryName(), is(equalTo(secCategoryName)));
        assertThat(getCategoryResponse.getBody().categoryResponses().get(1).positionId(), is(equalTo(secPositionId)));
        assertThat(getCategoryResponse.getBody().categoryResponses().get(1).categoryId(), is(equalTo(secSavedCategory.categoryId())));
    }

    @Test
    @DisplayName("Should get category and return 200 OK")
    void shouldGetCategory() {
        //given
        var categoryName = "Starters";
        var savedCategory = saveCategory(categoryName, POSITION_ID);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(getCategoryResponse.getBody(), is(not(nullValue())));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(categoryName)));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(POSITION_ID)));
        assertThat(getCategoryResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));
    }

    @Test
    @DisplayName("Should not add category by existing positionId and return 400 BAD REQUEST")
    void shouldNotAddCategory() {
        //given
        var categoryName = "Starters";
        var secCategoryName = "Coffees";
        var savedCategory = saveCategory(categoryName, POSITION_ID);
        var savedCategoryRequest = new CategoryRequest(POSITION_ID, secCategoryName);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody(), is(not(nullValue())));
        assertThat(getCategoryResponse.getBody().categoryName(), equalTo(categoryName));
        assertThat(getCategoryResponse.getBody().positionId(), equalTo(POSITION_ID));
        assertThat(getCategoryResponse.getBody().categoryId(), equalTo(savedCategory.categoryId()));

        //when
        var createCategoryResponse = client.postForEntity(
                prepareUrl(CATEGORY_RESOURCE),
                savedCategoryRequest,
                CategoryRequestResponse.class
        );

        //then
        assertThat(createCategoryResponse.getStatusCode(), equalTo(BAD_REQUEST));
    }

    @Test
    @DisplayName("Should not get category when Id is wrong and return 404 NOT FOUND")
    void shouldNotGetCategory() {
        //given
        var categoryName = "Cocktails";
        saveCategory(categoryName, POSITION_ID);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(WRONG_CATEGORY_ID),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(NOT_FOUND));
    }

    @Test
    @DisplayName("Should delete category by Id and return 200 OK")
    void shouldDeleteCategoryById() {
        //given
        var categoryName = "Desserts";
        var savedCategory = saveCategory(categoryName, POSITION_ID);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody(), is(notNullValue()));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(savedCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(savedCategory.positionId())));
        assertThat(getCategoryResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));

        //when
        client.delete(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        getCategoryResponse = client.getForEntity(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(NOT_FOUND));
    }

    @Test
    @DisplayName("Should not delete category by wrong Id and return 404 NOT FOUND")
    void shouldNotDeleteCategoryByWrongId() {
        //given
        var categoryName = "Desserts";
        var savedCategory = saveCategory(categoryName, POSITION_ID);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody(), is(notNullValue()));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(savedCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(savedCategory.positionId())));
        assertThat(getCategoryResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));

        //when
        client.delete(
                categoryPath(WRONG_CATEGORY_ID),
                CategoryResponse.class
        );

        getCategoryResponse = client.getForEntity(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody(), is(notNullValue()));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(savedCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(savedCategory.positionId())));
        assertThat(getCategoryResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));
    }

    @Test
    @DisplayName("Should update category by Id and return 200 OK")
    void shouldUpdateCategoryById() {
        //given
        var categoryName = "Drinks";
        var secCategoryName = "Salads";
        var secPositionId = 4L;
        var savedCategory = saveCategory(categoryName, POSITION_ID);
        var updatedCategory = getUpdatedCategoryRequest(secPositionId, secCategoryName);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody(), is(notNullValue()));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(savedCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(savedCategory.positionId())));
        assertThat(getCategoryResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));

        //when
        var updatedCategoryResponse = client.exchange(
                categoryPath(savedCategory.categoryId()),
                PUT,
                createBody(updatedCategory),
                CategoryResponse.class
        );

        //then
        assertThat(updatedCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(updatedCategoryResponse.getBody(), is(notNullValue()));
        assertThat(updatedCategoryResponse.getBody().categoryName(), is(equalTo(secCategoryName)));
        assertThat(updatedCategoryResponse.getBody().positionId(), is(equalTo(secPositionId)));
        assertThat(updatedCategoryResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));

        //when
        getCategoryResponse = client.getForEntity(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody(), is(notNullValue()));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(secCategoryName)));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(secPositionId)));
        assertThat(getCategoryResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));
    }

    @Test
    @DisplayName("Should not update category by wrong Id and return 404 NOT FOUND")
    void shouldNotUpdateCategoryByWrongId() {
        //given
        var categoryName = "Main Courses";
        var secCategoryName = "Entrees";
        var secPositionId = 4L;
        var savedCategory = saveCategory(categoryName, POSITION_ID);
        var updatedCategory = getUpdatedCategoryRequest(secPositionId, secCategoryName);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(savedCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody(), is(notNullValue()));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(savedCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(savedCategory.positionId())));
        assertThat(getCategoryResponse.getBody().categoryId(), is(equalTo(savedCategory.categoryId())));

        //when
        var updatedCategoryResponse = client.exchange(
                categoryPath(WRONG_CATEGORY_ID),
                PUT,
                createBody(updatedCategory),
                CategoryResponse.class
        );

        //then
        assertThat(updatedCategoryResponse.getStatusCode(), equalTo(NOT_FOUND));
    }
}