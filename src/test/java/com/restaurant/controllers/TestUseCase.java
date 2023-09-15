package com.restaurant.controllers;

import com.restaurant.app.App;
import com.restaurant.app.category.controller.dto.CategoryRequest;
import com.restaurant.app.category.controller.dto.CategoryRequestResponse;
import com.restaurant.app.category.controller.dto.UpdateCategoryRequest;
import com.restaurant.app.contact.controller.dto.ContactRequest;
import com.restaurant.app.contact.controller.dto.ContactRequestResponse;
import com.restaurant.app.food.controller.dto.FoodRequest;
import com.restaurant.app.food.controller.dto.FoodRequestResponse;
import com.restaurant.app.food.controller.dto.FoodRequestUpdate;
import com.restaurant.app.user.controller.LoginRequest;
import com.restaurant.app.user.controller.dto.LoginRequestResponse;
import com.restaurant.app.user.controller.dto.UserRequest;
import com.restaurant.app.user.controller.dto.UserRequestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static com.restaurant.app.response.ConstantValues.TOKEN_PREFIX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestUseCase {

    @Autowired
    protected TestRestTemplate client;

    @LocalServerPort
    protected int port;

    private String userToken;

    private String adminToken;

    private static final String USER_EMAIL = "mariuszek@user.pl";
    private static final String USER_PASSWORD = "user";
    private static final String ADMIN_EMAIL = "pro8l@admin.pl";
    private static final String ADMIN_PASSWORD = "admin";

    protected static final String BASE_URL = "http://localhost:%d%s";
    protected static final Long POSITION_ID = 1L;

    protected static final Long WRONG_ID = -1L;
    protected static final String CATEGORY_RESOURCE = "/categories";
    protected static final String CATEGORY_PATH = CATEGORY_RESOURCE + "/%d";

    protected static final String FOOD_RESOURCE = "/food";
    protected static final String FOOD_PATH = FOOD_RESOURCE + "/%d";

    protected static final String CONTACT_RESOURCE = "/contact";
    protected static final String CONTACT_PATH = CONTACT_RESOURCE + "/%d";

    protected static final String USER_ROLE = "USER";
    protected static final String USER_RESOURCE = "/users";
    protected static final String USER_PATH = USER_RESOURCE + "/%d";
    protected static final String LOGIN_PATH = USER_RESOURCE + "/login";
    protected static final String LOGOUT_PATH = USER_RESOURCE + "/logout";
    protected static final String PASSWORD_PATH = USER_RESOURCE + "/password";

    protected CategoryRequestResponse saveCategory(String categoryName, Long positionId) {
        //given
        var createCategoryRequest = new CategoryRequest(positionId, categoryName);

        runAsAdmin();

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

    protected FoodRequestUpdate createFoodRequestUpdate(Long categoryId, long positionId, String foodName, int foodPrice) {
        return new FoodRequestUpdate(categoryId, positionId, foodName, foodPrice);
    }

    protected FoodRequestResponse saveFood(Long categoryId, String foodName, Integer foodPrice, Long foodPositionId) {
        var foodRequest = createFoodRequest(foodPositionId, foodName, foodPrice);

        runAsAdmin();

        //when
        var foodResponse = client.postForEntity(
                prepareFoodUrlWithCategoryId(categoryId),
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

    @BeforeEach
    public void login() {
        userToken = getToken(USER_EMAIL, USER_PASSWORD);
        adminToken = getToken(ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    private String getToken(String email, String password) {
        var loginRequest = new LoginRequest(email, password);
        var userToken = client.postForEntity(
                prepareUrl(LOGIN_PATH),
                loginRequest,
                LoginRequestResponse.class
        );

        assertThat(userToken.getStatusCode(), is(equalTo(OK)));
        assertThat(userToken.getBody(), is(notNullValue()));

        return userToken.getBody().token();
    }

    protected void runAsAdmin() {
        addToken(adminToken);
    }

    protected void runAsUser() {
        addToken(userToken);
    }

    protected void runAsUserWithData(String email, String password) {
        var token = getToken(email, password);
        addToken(token);
    }

    protected void addToken(String token) {
        client.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders().add("Authorization",
                            String.format("%s%s", TOKEN_PREFIX, token));
                    return execution.execute(request, body);
                })
        );
    }

    protected UserRequestResponse saveUser(UserRequest userRequest) {
        //when
        var userResponse = client.postForEntity(
                prepareUrl(USER_RESOURCE),
                userRequest,
                UserRequestResponse.class
        );

        //then
        assertThat(userResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(userResponse.getBody(), is(notNullValue()));
        assertThat(userResponse.getBody().email(), is(equalTo(userRequest.email())));
        assertThat(userResponse.getBody().name(), is(equalTo(userRequest.name())));
        assertThat(userResponse.getBody().role(), is(equalTo(USER_ROLE)));
        assertThat(userResponse.getBody().address(), is(equalTo(userRequest.address())));
        assertThat(userResponse.getBody().phoneNumber(), is(equalTo(userRequest.phoneNumber())));

        return userResponse.getBody();
    }

    protected String prepareUrl(String resource) {
        return String.format(BASE_URL, port, resource);
    }

    protected String prepareFoodUrlWithCategoryId(Long categoryId) {
        return prepareUrl(String.format(CATEGORY_PATH, categoryId) + FOOD_RESOURCE);
    }

    protected String prepareFoodUrlWithFoodIdAndCategoryId(Long categoryId, Long foodId) {
        return prepareCategoryUrlWithCategoryId(categoryId) + String.format(FOOD_PATH, foodId);
    }

    protected String prepareUserUrlWithUserId(Long userId) {
        return prepareUrl(String.format(USER_PATH, userId));
    }

    protected String prepareCategoryUrlWithCategoryId(Long categoryId) {
        return prepareUrl(String.format(CATEGORY_PATH, categoryId));
    }

    protected String prepareContactUrlWithContactId(Long contactId) {
        return prepareUrl(String.format(CONTACT_PATH, contactId));
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

        runAsAdmin();

        //when
        var contactRequestResponse = client.postForEntity(
                prepareUrl(CONTACT_RESOURCE),
                contactRequest,
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

    protected FoodRequest createFoodRequest(Long foodPosition, String foodName, Integer foodPrice) {
        return new FoodRequest(foodPosition, foodName, foodPrice);
    }

    protected <T> HttpEntity<Object> createBody(T body) {
        return new HttpEntity<>(body);
    }
}
