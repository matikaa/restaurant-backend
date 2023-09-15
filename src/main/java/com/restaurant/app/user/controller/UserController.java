package com.restaurant.app.user.controller;

import com.restaurant.app.user.controller.dto.*;
import com.restaurant.app.user.controller.validator.UserValidator;
import com.restaurant.app.user.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.restaurant.app.response.ConstantValues.*;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final UserControllerMapper userControllerMapper = UserControllerMapper.INSTANCE;

    private final UserValidator userValidator;

    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userValidator = new UserValidator();
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserListResponse> getUsers() {
        return ResponseEntity.ok().body(new UserListResponse(
                userControllerMapper.usersToUserResponses(
                        userService.getAll()).stream().toList()));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        if (!userService.existsByUserId(userId)) {
            LOGGER.warn(USER_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return userService.getUserById(userId)
                .map(userControllerMapper::userToUserResponse)
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserRequestResponse> addUser(@RequestBody UserRequest userRequest) {
        if (userValidator.isUserRequestNotValid(userRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (!userService.isValidEmail(userRequest.email())) {
            LOGGER.warn(WRONG_EMAIL);
            return ResponseEntity.badRequest().build();
        }

        if (userService.existsUserByEmail(userRequest.email())) {
            LOGGER.warn(EMAIL_EXISTS);
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.status(CREATED).body(
                userControllerMapper.userToUserRequestResponse(userService.insert(userRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRequestResponse> login(@RequestBody LoginRequest loginRequest) {
        if (userValidator.isLoginRequestNotValid(loginRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (userService.verifyAndLogUser(loginRequest)) {
            return ResponseEntity.ok().body(
                    userControllerMapper.stringToLoginRequestResponse(
                            userService.generateToken(loginRequest.email())));
        } else {
            LOGGER.warn(INCORRECT_LOG_DATA);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Transactional
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (userValidator.isStringNotValid(authorizationHeader)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        userService.logout(authorizationHeader);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserResponse> updateUserDetails(@RequestBody UpdateUserRequest updateUserRequest) {
        if (userValidator.isUpdateUserRequestNotValid(updateUserRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        var changedUser = userService.changeUserDetails(userEmail, updateUserRequest);
        if (!changedUser.isPresent()) {
            LOGGER.warn(USER_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(userControllerMapper.userToUserResponse(changedUser.get()));
    }

    @PostMapping("/me")
    @PreAuthorize("hasAuthority('USER')")
    @Transactional
    public ResponseEntity<Void> deleteAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                              @RequestBody PasswordRequest passwordRequest) {
        if (userValidator.isPasswordRequestNotValid(passwordRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userService.verifyPassword(userEmail, passwordRequest.password())) {
            LOGGER.warn(INCORRECT_PASSWORD);
            return ResponseEntity.badRequest().build();
        }

        if (!userService.existsUserByEmail(userEmail)) {
            LOGGER.warn(USER_NOT_EXISTS);
            ResponseEntity.notFound().build();
        }

        userService.logout(authorizationHeader);
        userService.deleteByEmail(userEmail);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUserByAdmin(@PathVariable Long userId) {
        if (!userService.existsByUserId(userId)) {
            LOGGER.warn(USER_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(userId);

        return ResponseEntity.ok().build();
    }


    @PutMapping("/me/password")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (userValidator.isChangePasswordRequestNotValid(changePasswordRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        var changedPassword = userService.changePassword(
                userEmail, changePasswordRequest);

        if (!changedPassword) {
            LOGGER.warn(INCORRECT_PASSWORD);
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/password")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> changeAnyUserPassword(@PathVariable Long userId,
                                                      @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        if (userValidator.isUserChangePasswordRequestNotValid(userChangePasswordRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (!userService.changeUserPassword(userId, userChangePasswordRequest)) {
            LOGGER.warn(USER_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
