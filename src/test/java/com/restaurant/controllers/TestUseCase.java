package com.restaurant.controllers;

import com.restaurant.app.App;
import com.restaurant.app.category.controller.dto.CategoryRequest;
import com.restaurant.app.category.controller.dto.CategoryRequestResponse;
import com.restaurant.app.category.controller.dto.UpdateCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestUseCase {

    @Autowired
    protected TestRestTemplate client;

    @LocalServerPort
    protected int port;

    private static final String BASE_URL_FORMAT = "http://localhost:%d%s";
    protected static final String CATEGORY_RESOURCE = "/categories";
    private static final String CATEGORY_PATH = CATEGORY_RESOURCE + "/%d";
    public static final Long POSITION_ID = 1L;
    public static final Long WRONG_CATEGORY_ID = -1L;

    protected CategoryRequestResponse createCategory(String categoryName, Long positionId) {
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

    protected UpdateCategoryRequest getUpdatedCategoryRequest(Long positionId, String categoryName) {
        return new UpdateCategoryRequest(positionId, categoryName);
    }

    protected String prepareUrl(String resource) {
        return String.format(BASE_URL_FORMAT, port, resource);
    }

    protected String categoryPath(Long categoryId) {
        return prepareUrl(String.format(CATEGORY_PATH, categoryId));
    }

    protected <T> HttpEntity<Object> createBody(T body) {
        return new HttpEntity<>(body);
    }
}
