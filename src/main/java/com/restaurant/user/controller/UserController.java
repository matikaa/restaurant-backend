package com.restaurant.user.controller;

import com.restaurant.common.ConstantValues;
import com.restaurant.user.controller.dto.*;
import com.restaurant.user.controller.validator.UserValidator;
import com.restaurant.user.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

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

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserResponse> getUserByContext() {
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return userService.getUserByEmail(userEmail)
                .map(userControllerMapper::userToUserResponse)
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        if (!userService.existsByUserId(userId)) {
            LOGGER.warn(ConstantValues.USER_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return userService.getUserById(userId)
                .map(userControllerMapper::userToUserResponse)
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/role")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserRole> getUserRole(@PathVariable Long userId) {
        if (!userService.existsByUserId(userId)) {
            LOGGER.warn(ConstantValues.USER_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return userService.getUserById(userId)
                .map(userControllerMapper::userToUserRole)
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/balance")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserMoney> getCurrentUserBalance(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(userControllerMapper::userToUserMoney)
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserRequestResponse> register(@RequestBody UserRequest userRequest) {
        if (userValidator.isUserRequestNotValid(userRequest)) {
            LOGGER.warn(ConstantValues.INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (!userService.isValidEmail(userRequest.email())) {
            LOGGER.warn(ConstantValues.WRONG_EMAIL);
            return ResponseEntity.badRequest().build();
        }

        if (userService.existsUserByEmail(userRequest.email())) {
            LOGGER.warn(ConstantValues.EMAIL_EXISTS);
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.status(CREATED).body(
                userControllerMapper.userToUserRequestResponse(userService.insert(userRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRequestResponse> login(@RequestBody LoginRequest loginRequest) {
        if (userValidator.isLoginRequestNotValid(loginRequest)) {
            LOGGER.warn(ConstantValues.INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (userService.verifyAndLogUser(loginRequest)) {
            return ResponseEntity.ok().body(
                    userControllerMapper.userLoginToLoginRequestResponse(
                            userService.generateToken(loginRequest.email())));
        } else {
            LOGGER.warn(ConstantValues.INCORRECT_LOG_DATA);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Transactional
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (userValidator.isStringNotValid(authorizationHeader)) {
            LOGGER.warn(ConstantValues.INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        userService.logout(authorizationHeader);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserResponse> updateUserDetails(@RequestBody UpdateUserRequest updateUserRequest) {
        if (userValidator.isUpdateUserRequestNotValid(updateUserRequest)) {
            LOGGER.warn(ConstantValues.INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        var changedUser = userService.changeUserDetails(userEmail, updateUserRequest);
        if (changedUser.isEmpty()) {
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        return ResponseEntity.ok().body(userControllerMapper.userToUserResponse(changedUser.get()));
    }

    @PostMapping("/me")
    @PreAuthorize("hasAuthority('USER')")
    @Transactional
    public ResponseEntity<Void> deleteAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                              @RequestBody PasswordRequest passwordRequest) {
        if (userValidator.isPasswordRequestNotValid(passwordRequest)) {
            LOGGER.warn(ConstantValues.INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userService.existsUserByEmail(userEmail)) {
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        if (!userService.verifyPassword(userEmail, passwordRequest.password())) {
            LOGGER.warn(ConstantValues.INCORRECT_PASSWORD);
            return ResponseEntity.badRequest().build();
        }

        userService.logout(authorizationHeader);
        userService.deleteByEmail(userEmail);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUserByAdmin(@PathVariable Long userId) {
        if (!userService.existsByUserId(userId)) {
            LOGGER.warn(ConstantValues.USER_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(userId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/password")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (userValidator.isChangePasswordRequestNotValid(changePasswordRequest)) {
            LOGGER.warn(ConstantValues.INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!userService.existsUserByEmail(userEmail)) {
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        var changedPassword = userService.changePassword(
                userEmail, changePasswordRequest);

        if (!changedPassword) {
            LOGGER.warn(ConstantValues.INCORRECT_PASSWORD);
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/password")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> changeAnyUserPassword(@PathVariable Long userId,
                                                      @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        if (userValidator.isUserChangePasswordRequestNotValid(userChangePasswordRequest)) {
            LOGGER.warn(ConstantValues.INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (!userService.changeUserPassword(userId, userChangePasswordRequest)) {
            LOGGER.warn(ConstantValues.USER_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/balance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Void> changeAnyUserAccountBalance(@PathVariable Long userId,
                                                            @RequestBody @Validated UserMoney userMoney) {
        if (!userService.existsByUserId(userId)) {
            LOGGER.warn(ConstantValues.USER_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        if(userMoney.money() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        userService.updateUserBalance(userId, userMoney);

        return ResponseEntity.ok().build();
    }
}
