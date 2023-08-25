package com.restaurant.controllers;

import com.restaurant.app.App;
import com.restaurant.app.category.controller.dto.CategoryRequest;
import com.restaurant.app.category.controller.dto.CategoryRequestResponse;
import com.restaurant.app.category.controller.dto.UpdateCategoryRequest;
import com.restaurant.app.contact.controller.dto.ContactRequest;
import com.restaurant.app.contact.controller.dto.ContactRequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestUseCase {

    @Autowired
    protected TestRestTemplate client;

    @LocalServerPort
    protected int port;

    protected static final Long WRONG_CATEGORY_ID = -1L;
    protected static final String CONTACT_URL = "/contact";
    protected static final Long POSITION_ID = 1L;
    protected static final String CATEGORY_RESOURCE = "/categories";

    protected static final String CATEGORY_PATH = CATEGORY_RESOURCE + "/%d";
    private static final String GET_CONTACT_URL = CONTACT_URL + "/%d";
    private static final String BASE_URL = "http://localhost:%d%s";

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
        return String.format(BASE_URL, port, resource);
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
        return client.postForEntity(
                prepareUrl(CONTACT_URL), createContactRequest(), ContactRequestResponse.class).getBody();
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

    protected <T> HttpEntity<Object> createBody(T body) {
        return new HttpEntity<>(body);
    }
}
