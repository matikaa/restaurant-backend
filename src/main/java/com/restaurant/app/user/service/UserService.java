package com.restaurant.app.user.service;

import com.restaurant.app.user.controller.LoginRequest;
import com.restaurant.app.user.controller.dto.ChangePasswordRequest;
import com.restaurant.app.user.controller.dto.UserRequest;
import com.restaurant.app.user.service.dto.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long userId);

    List<User> getAll();

    boolean existsByUserId(Long userId);

    User insert(UserRequest userRequest);

    boolean existsUserByEmail(String email);

    boolean verifyAndLogUser(LoginRequest loginRequest);

    void logout(String token);

    String generateToken(String email);

    boolean changeUserPassword(String email, ChangePasswordRequest changePasswordRequest);

    boolean isValidEmail(String email);
}
