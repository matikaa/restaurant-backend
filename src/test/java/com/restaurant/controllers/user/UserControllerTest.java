package com.restaurant.controllers.user;

import com.restaurant.app.user.controller.LoginRequest;
import com.restaurant.app.user.controller.dto.*;
import com.restaurant.controllers.TestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

class UserControllerTest extends TestUseCase {

    @Test
    @DisplayName("Should create user and return 200 OK")
    void shouldCreateUserAndReturnOk() {
        //given
        var email = "example@com.pl";
        var name = "Matthew";
        var password = "t34tad#$!fAD";
        var address = "ul. Glowna 40, 60-001 Poznan";
        var phoneNumber = "534567890";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);

        //when
        var userResponse = client.postForEntity(
                prepareUrl(USER_RESOURCE),
                createUserRequest,
                UserRequestResponse.class
        );

        //then
        assertThat(userResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(userResponse.getBody(), is(notNullValue()));
        assertThat(userResponse.getBody().email(), is(equalTo(email)));
        assertThat(userResponse.getBody().name(), is(equalTo(name)));
        assertThat(userResponse.getBody().role(), is(equalTo(USER_ROLE)));
        assertThat(userResponse.getBody().address(), is(equalTo(address)));
        assertThat(userResponse.getBody().phoneNumber(), is(equalTo(phoneNumber)));
    }

    @Test
    @DisplayName("Should not create user by wrong email and return 400 BAD REQUEST")
    void shouldNotCreateUserByWrongEmailAndReturnBadRequest() {
        //given
        var email = "@example.com";
        var name = "John";
        var password = "t34tad#$!fAD";
        var address = "Pl. Grunwaldzki 45, 80-001 Gdansk";
        var phoneNumber = "538590123";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);

        //when
        var userResponse = client.postForEntity(
                prepareUrl(USER_RESOURCE),
                createUserRequest,
                String.class
        );

        //then
        assertThat(userResponse.getStatusCode(), is(equalTo(BAD_REQUEST)));
    }

    @Test
    @DisplayName("Should not create user by existing email and return 409 CONFLICT")
    void shouldNotCreateUserByExistingEmailAndReturnConflict() {
        //given
        var email = "mary.smith@mail.com";
        var name = "Mary";
        var password = "t34tad#$!fAD";
        var address = "ul. Mickiewicza 10, 50-001 Wroclaw";
        var phoneNumber = "530712345";
        var secEmail = "mary.smith@mail.com";
        var secName = "Matthew";
        var secPassword = "t34tad#$!fAD";
        var secAddress = "ul. Dluga 15, 70-001 Szczecin";
        var secPhoneNumber = "532934567";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);

        //when
        var userResponse = client.postForEntity(
                prepareUrl(USER_RESOURCE),
                createUserRequest,
                UserRequestResponse.class
        );

        //then
        assertThat(userResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(userResponse.getBody(), is(notNullValue()));
        assertThat(userResponse.getBody().email(), is(equalTo(email)));
        assertThat(userResponse.getBody().name(), is(equalTo(name)));
        assertThat(userResponse.getBody().role(), is(equalTo(USER_ROLE)));

        //given
        var secCreateUserRequest = new UserRequest(secEmail, secName, secPassword, secAddress, secPhoneNumber);

        //when
        var secUserResponse = client.postForEntity(
                prepareUrl(USER_RESOURCE),
                secCreateUserRequest,
                String.class
        );

        //then
        assertThat(secUserResponse.getStatusCode(), is(equalTo(CONFLICT)));
    }

    @Test
    @DisplayName("Should get users and return 200 OK")
    void shouldGetUsersAndReturnOk() {
        //given
        var email = "noah.brown@hotmail.com";
        var name = "Noah";
        var password = "t34tad#$!fAD";
        var address = "ul. Kosciuszki 20, 90-001 Lodz";
        var phoneNumber = "533045678";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);
        var createdUser = saveUser(createUserRequest);

        //when
        runAsAdmin();
        var userListResponse = client.getForEntity(
                prepareUrl(USER_RESOURCE),
                UserListResponse.class
        );

        //then
        assertThat(userListResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(userListResponse.getBody(), is(notNullValue()));

        var foundUser = userListResponse.getBody().userResponses()
                .stream().filter(user -> user.email().equals(email))
                .findAny();

        assertThat(foundUser, is(not(Optional.empty())));
        assertThat(createdUser.name(), is(equalTo(foundUser.get().name())));
        assertThat(createdUser.email(), is(equalTo(foundUser.get().email())));
        assertThat(createdUser.role(), is(equalTo(foundUser.get().role())));
        assertThat(createdUser.userId(), is(equalTo(foundUser.get().userId())));
        assertThat(createdUser.createdTime(), is(equalTo(foundUser.get().createdTime())));
    }

    @Test
    @DisplayName("Should get user by id and return 200 OK")
    void shouldGetUserByIdAndReturnOk() {
        //given
        var email = "ava.davis@outlook.com";
        var name = "Ava";
        var password = "t34tad#$!fAD";
        var address = "Pl. Teatralny 25, 00-001 Warszawa";
        var phoneNumber = "534156789";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);
        var createdUser = saveUser(createUserRequest);

        //when
        runAsAdmin();
        var userResponse = client.getForEntity(
                prepareUserUrlWithUserId(createdUser.userId()),
                UserResponse.class
        );

        //then
        assertThat(userResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(userResponse.getBody(), is(notNullValue()));
        assertThat(userResponse.getBody().userId(), is(equalTo(createdUser.userId())));
        assertThat(userResponse.getBody().role(), is(equalTo(createdUser.role())));
        assertThat(userResponse.getBody().name(), is(equalTo(createdUser.name())));
        assertThat(userResponse.getBody().email(), is(equalTo(createdUser.email())));
        assertThat(userResponse.getBody().createdTime(), is(equalTo(createdUser.createdTime())));
    }

    @Test
    @DisplayName("Should not get user by wrong id and return 404 NOT FOUND")
    void shouldNotGetUserByWrongIdAndReturnNotFound() {
        //given
        var email = "sophia.wilson@gmail.com";
        var name = "Sophia";
        var password = "t34tad#$!fAD";
        var address = "ul. Pilsudskiego 30, 30-001 Krakow";
        var phoneNumber = "535267890";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);
        saveUser(createUserRequest);

        //when
        runAsAdmin();
        var userResponse = client.getForEntity(
                prepareUserUrlWithUserId(WRONG_ID),
                String.class
        );

        //then
        assertThat(userResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should login user and return 200 OK")
    void shouldLoginUserAndReturnOk() {
        //given
        var email = "oliver.jackson@yahoo.com";
        var name = "Oliver";
        var password = "t34tad#$!fAD";
        var address = "Pl. Solny 35, 50-001 Wroclaw";
        var phoneNumber = "536378901";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);
        saveUser(createUserRequest);

        var loginRequest = new LoginRequest(email, password);

        //when
        var loginResponse = client.postForEntity(
                prepareUrl(LOGIN_PATH),
                loginRequest,
                LoginRequestResponse.class
        );

        //then
        assertThat(loginResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(loginResponse.getBody(), is(notNullValue()));
    }

    @Test
    @DisplayName("Should not login user by wrong email and return 404 NOT FOUND")
    void shouldNotLoginUserByWrongEmailAndReturnNotFound() {
        //given
        var email = "mia.martinez@hotmail.com";
        var wrongEmail = "mono@outlook.com";
        var name = "Mia";
        var password = "t34tad#$!fAD";
        var address = "ul. Sw. Wojciecha 50, 70-001 Szczecin";
        var phoneNumber = "539601234";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);
        saveUser(createUserRequest);

        var loginRequest = new LoginRequest(wrongEmail, password);

        //when
        var loginResponse = client.postForEntity(
                prepareUrl(LOGIN_PATH),
                loginRequest,
                String.class
        );

        //then
        assertThat(loginResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should not login user by wrong password and return 404 NOT FOUND")
    void shouldNotLoginUserByWrongPasswordAndReturnNotFound() {
        //given
        var email = "elijah.taylor@outlook.com";
        var name = "Elijah";
        var password = "t34tad#$!fAD";
        var wrongPassword = "sdo3@$4G";
        var address = "ul. Piotrkowska 55, 90-001 Lodz";
        var phoneNumber = "530712345";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);
        saveUser(createUserRequest);

        var loginRequest = new LoginRequest(email, wrongPassword);

        //when
        var loginResponse = client.postForEntity(
                prepareUrl(LOGIN_PATH),
                loginRequest,
                String.class
        );

        //then
        assertThat(loginResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should logout user and return 200 OK")
    void shouldLogoutUserAndReturnOk() {
        runAsUser();

        //when
        var logoutResponse = client.postForEntity(
                prepareUrl(LOGOUT_PATH),
                null,
                Void.class
        );

        //then
        assertThat(logoutResponse.getStatusCode(), is(equalTo(OK)));
    }

    @Test
    @DisplayName("Should logout admin and return 200 OK")
    void shouldLogoutAdminAndReturnOk() {
        runAsAdmin();

        //when
        var logoutResponse = client.postForEntity(
                prepareUrl(LOGOUT_PATH),
                null,
                Void.class
        );

        //then
        assertThat(logoutResponse.getStatusCode(), is(equalTo(OK)));
    }

    @Test
    @DisplayName("Should not logout not logged in user and return 400 BAD REQUEST")
    void shouldNotLogoutNotLoggedInUserAndReturnBadRequest() {
        //when
        var logoutResponse = client.postForEntity(
                prepareUrl(LOGOUT_PATH),
                null,
                Void.class
        );

        //then
        assertThat(logoutResponse.getStatusCode(), is(equalTo(BAD_REQUEST)));
    }

    @Test
    @DisplayName("Should change user password and return 200 OK")
    void shouldChangeUserPasswordAndReturnOk() {
        //given
        var email = "charlotte.thomas@gmail.com";
        var name = "Charlotte";
        var password = "t34tad#$!fAD";
        var newPassword = "sda$@$daSDA2#";
        var address = "Pl. Zamkowy 60, 00-001 Warszawa";
        var phoneNumber = "531823456";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);
        saveUser(createUserRequest);

        var changedPasswordRequest = new ChangePasswordRequest(password, newPassword);

        //when
        runAsUserWithData(email, password);

        var changedPasswordResponse = client.exchange(
                prepareUrl(PASSWORD_PATH),
                PUT,
                createBody(changedPasswordRequest),
                Void.class
        );

        //then
        assertThat(changedPasswordResponse.getStatusCode(), is(equalTo(OK)));

        //given
        var loginRequest = new LoginRequest(email, newPassword);

        //when
        var loginResponse = client.postForEntity(
                prepareUrl(LOGIN_PATH),
                loginRequest,
                String.class
        );

        //then
        assertThat(loginResponse.getStatusCode(), is(equalTo(OK)));
    }

    @Test
    @DisplayName("Should not change user password by wrong password and return 409 CONFLICT")
    void shouldNotChangeUserPasswordByWrongPasswordAndReturnConflict() {
        //given
        var email = "james.anderson@yahoo.com";
        var name = "James";
        var password = "t34tad#$!fAD";
        var wrongPassword = "da###2s!";
        var newPassword = "sda$@$daSDA2#";
        var address = "ul. Florianska 70, 30-001 Krakow";
        var phoneNumber = "533045678";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);
        saveUser(createUserRequest);

        var changedPasswordRequest = new ChangePasswordRequest(wrongPassword, newPassword);

        //when
        runAsUserWithData(email, password);

        var changedPasswordResponse = client.exchange(
                prepareUrl(PASSWORD_PATH),
                PUT,
                createBody(changedPasswordRequest),
                String.class
        );

        //then
        assertThat(changedPasswordResponse.getStatusCode(), is(equalTo(CONFLICT)));
    }

    @Test
    @DisplayName("Should not change user password by same password and return 409 CONFLICT")
    void shouldNotChangeUserPasswordBySamePasswordAndReturnConflict() {
        //given
        var email = "amelia.thomas@o2.pl";
        var name = "Amelia";
        var password = "t34tad#$!fAD";
        var address = "ul. Morska 90, 60-001 Poznan";
        var phoneNumber = "534156789";

        var createUserRequest = new UserRequest(email, name, password, address, phoneNumber);
        saveUser(createUserRequest);

        var changedPasswordRequest = new ChangePasswordRequest(password, password);

        //when
        runAsUserWithData(email, password);

        var changedPasswordResponse = client.exchange(
                prepareUrl(PASSWORD_PATH),
                PUT,
                createBody(changedPasswordRequest),
                String.class
        );

        //then
        assertThat(changedPasswordResponse.getStatusCode(), is(equalTo(CONFLICT)));
    }
}
