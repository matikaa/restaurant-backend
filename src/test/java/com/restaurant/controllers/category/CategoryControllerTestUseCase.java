package com.restaurant.controllers.category;

import com.restaurant.app.category.controller.dto.CategoryListResponse;
import com.restaurant.app.category.controller.dto.CategoryResponse;
import com.restaurant.controllers.TestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class CategoryControllerTestUseCase extends TestUseCase {

    @Test
    @DisplayName("Should get categories and return 200 OK")
    void shouldGetCategories() {
        //given
        var categoryName = "Juices";
        var secCategoryName = "Soups";
        createCategory(categoryName);
        createCategory(secCategoryName);

        //when
        var getCategoryResponse = client.getForEntity(
                prepareUrl(CATEGORY_RESOURCE),
                CategoryListResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody(), is(not(nullValue())));
        assertThat(getCategoryResponse.getBody().categoryResponses().size(), equalTo(2));
        assertThat(getCategoryResponse.getBody().categoryResponses().get(0).categoryName(), equalTo(categoryName));
        assertThat(getCategoryResponse.getBody().categoryResponses().get(1).categoryName(), equalTo(secCategoryName));
    }

    @Test
    @DisplayName("Should get category and return 200 OK")
    void shouldGetCategory() {
        //given
        var categoryName = "Starters";
        var createdCategory = createCategory(categoryName);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(createdCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody(), is(not(nullValue())));
        assertThat(getCategoryResponse.getBody().categoryName(), equalTo(categoryName));
    }

    @Test
    @DisplayName("Should not get category when Id is wrong and return 404 NOT FOUND")
    void shouldNotGetCategory() {
        //given
        var categoryName = "Cocktails";
        createCategory(categoryName);

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
        var createdCategory = createCategory(categoryName);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(createdCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(createdCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(createdCategory.positionId())));

        //when
        client.delete(
                categoryPath(createdCategory.categoryId()),
                CategoryResponse.class
        );

        getCategoryResponse = client.getForEntity(
                categoryPath(createdCategory.categoryId()),
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
        var createdCategory = createCategory(categoryName);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(createdCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(createdCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(createdCategory.positionId())));

        //when
        client.delete(
                categoryPath(WRONG_CATEGORY_ID),
                CategoryResponse.class
        );

        getCategoryResponse = client.getForEntity(
                categoryPath(createdCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(createdCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(createdCategory.positionId())));
    }

    @Test
    @DisplayName("Should update category by Id and return 200 OK")
    void shouldUpdateCategoryById() {
        //given
        var positionId = 4L;
        var categoryName = "Drinks";
        var secCategoryName = "Salads";
        var createdCategory = createCategory(categoryName);
        var updatedCategory = getUpdatedCategoryRequest(positionId, secCategoryName);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(createdCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(createdCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(createdCategory.positionId())));

        //when
        var updatedCategoryResponse = client.exchange(
                categoryPath(createdCategory.categoryId()),
                PUT,
                createBody(updatedCategory),
                CategoryResponse.class
        );

        getCategoryResponse = client.getForEntity(
                categoryPath(createdCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(secCategoryName)));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(positionId)));
    }

    @Test
    @DisplayName("Should not update category by wrong Id and return 404 NOT FOUND")
    void shouldNotUpdateCategoryByWrongId() {
        //given
        var positionId = 4L;
        var categoryName = "Main Courses";
        var secCategoryName = "Entrees";
        var createdCategory = createCategory(categoryName);
        var updatedCategory = getUpdatedCategoryRequest(positionId, secCategoryName);

        //when
        var getCategoryResponse = client.getForEntity(
                categoryPath(createdCategory.categoryId()),
                CategoryResponse.class
        );

        //then
        assertThat(getCategoryResponse.getStatusCode(), equalTo(OK));
        assertThat(getCategoryResponse.getBody().categoryName(), is(equalTo(createdCategory.categoryName())));
        assertThat(getCategoryResponse.getBody().positionId(), is(equalTo(createdCategory.positionId())));

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
