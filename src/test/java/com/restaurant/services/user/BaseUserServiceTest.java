package com.restaurant.services.user;

import com.restaurant.services.BaseTestUseCase;
import com.restaurant.user.controller.dto.LoginRequest;
import com.restaurant.user.controller.dto.UpdateUserRequest;
import com.restaurant.user.controller.dto.UserRequest;
import com.restaurant.user.repository.UserRepository;
import com.restaurant.user.repository.dto.UserModel;
import com.restaurant.user.service.BaseUserService;
import com.restaurant.user.service.dto.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseUserServiceTest extends BaseTestUseCase {

    @InjectMocks
    private BaseUserService baseUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("Should return list of all users")
    void shouldReturnListOfAllUsers() {
        //given
        User user = getWithUser();
        List<User> users = new ArrayList<>();
        users.add(user);

        UserModel userModel = getUserModel();
        List<UserModel> userModels = new ArrayList<>();
        userModels.add(userModel);

        when(userRepository.findAll()).thenReturn(userModels);

        //when
        List<User> usersResult = baseUserService.getAll();

        //then
        assertEquals(users, usersResult);
    }

    @Test
    @DisplayName("Should get user by userId")
    void shouldGetUserByUserId() {
        //given
        Long userId = 1L;
        Optional<User> user = Optional.of(getWithUser());
        Optional<UserModel> userModel = Optional.of(getUserModel());

        when(userRepository.findById(userId)).thenReturn(userModel);

        //when
        Optional<User> userResult = baseUserService.getUserById(userId);

        //then
        assertEquals(user, userResult);
    }

    @Test
    @DisplayName("Should get user by email")
    void shouldGetUserByEmail() {
        //given
        String userEmail = "michael@gmail.com";
        Optional<User> user = Optional.of(getWithUser());
        Optional<UserModel> userModel = Optional.of(getUserModel());

        when(userRepository.findByEmail(userEmail)).thenReturn(userModel);

        //when
        Optional<User> userResult = baseUserService.getUserByEmail(userEmail);

        //then
        assertEquals(user, userResult);
    }

    @Test
    @DisplayName("Should add and return user")
    void shouldAddAndReturnUser() {
        //given
        UserRequest userRequest = getUserRequest();
        UserModel userModelToSave = getUserModelToSave();
        User user = getWithUser();
        UserModel userModelSaved = getUserModel();

        when(userRepository.save(userModelToSave)).thenReturn(userModelSaved);

        //when
        User userResult = baseUserService.insert(userRequest);

        //then
        assertEquals(user, userResult);
    }

    @Test
    @DisplayName("Should verify and log user")
    void shouldVerifyAndLogUser() {
        //given
        String email = "matty@email.com";
        String password = "password@123";
        boolean userVerified = true;

        LoginRequest loginRequest = new LoginRequest(email, password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = getAuthentication();

        when(authenticationManager.authenticate(token)).thenReturn(authentication);

        //when
        boolean loginResult = baseUserService.verifyAndLogUser(loginRequest);

        //then
        assertEquals(userVerified, loginResult);
    }

    @Test
    @DisplayName("Should change user details")
    void shouldChangeUserDetails() {
        //given
        String email = "michael@gmail.com";
        Optional<UserModel> userModel = Optional.of(getUserModel());
        UpdateUserRequest updateUserRequest = getUpdateUserRequest();
        Optional<User> userUpdated = Optional.of(getWithUser());

        when(userRepository.changeUser(email, updateUserRequest)).thenReturn(userModel);

        //when
        Optional<User> userUpdateResult = baseUserService.changeUserDetails(email, updateUserRequest);

        //then
        assertEquals(userUpdated, userUpdateResult);
    }


}
