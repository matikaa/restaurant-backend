package com.restaurant.app.user.controller;

import com.restaurant.app.response.ResponseHandler;
import com.restaurant.app.user.controller.dto.ChangePasswordRequest;
import com.restaurant.app.user.controller.dto.UserListResponse;
import com.restaurant.app.user.controller.dto.UserRequest;
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

import static com.restaurant.app.response.StateValues.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final UserControllerMapper userControllerMapper = UserControllerMapper.INSTANCE;

    private final UserService userService;

    private final UserValidator userValidator;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userValidator = new UserValidator();
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok().body(new UserListResponse(
                userControllerMapper.usersToUserResponses(
                        userService.getAll()).stream().toList()));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        if (!userService.existsByUserId(userId)) {
            LOGGER.warn(USER_NOT_EXISTS);
            return ResponseHandler.generateError(USER_NOT_EXISTS, NOT_FOUND);
        }

        return userService.getUserById(userId)
                .map(userControllerMapper::userToUserResponse)
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody UserRequest userRequest) {
        if (userValidator.isUserRequestNotValid(userRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseHandler.generateError(INVALID_REQUEST_BODY, BAD_REQUEST);
        }

        if (!userService.isValidEmail(userRequest.email())) {
            LOGGER.warn(WRONG_EMAIL);
            return ResponseHandler.generateError(WRONG_EMAIL, BAD_REQUEST);
        }

        if (userService.existsUserByEmail(userRequest.email())) {
            LOGGER.warn(EXISTING_EMAIL);
            return ResponseHandler.generateError(EXISTING_EMAIL, CONFLICT);
        }

        return ResponseEntity.status(CREATED).body(
                userControllerMapper.userToUserRequestResponse(userService.insert(userRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (userValidator.isLoginRequestNotValid(loginRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseHandler.generateError(INVALID_REQUEST_BODY, BAD_REQUEST);
        }

        if (userService.verifyAndLogUser(loginRequest)) {
            return ResponseEntity.ok().body(
                    userControllerMapper.stringToLoginRequestResponse(
                            userService.generateToken(loginRequest.email())));
        } else {
            LOGGER.warn(INCORRECT_LOG_DATA);
            return ResponseHandler.generateError(INCORRECT_LOG_DATA, NOT_FOUND);
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Transactional
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (userValidator.isStringNotValid(authorizationHeader)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseHandler.generateError(INVALID_REQUEST_BODY, BAD_REQUEST);
        }

        userService.logout(authorizationHeader);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (userValidator.isChangePasswordRequestNotValid(changePasswordRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseHandler.generateError(INVALID_REQUEST_BODY, BAD_REQUEST);
        }
        var userMail = SecurityContextHolder.getContext().getAuthentication().getName();

        var changedPassword = userService.changeUserPassword(
                userMail, changePasswordRequest);

        if (!changedPassword) {
            LOGGER.warn(INCORRECT_PASSWORD);
            return ResponseHandler.generateError(INCORRECT_PASSWORD, CONFLICT);
        }

        return ResponseEntity.ok().build();
    }
}
