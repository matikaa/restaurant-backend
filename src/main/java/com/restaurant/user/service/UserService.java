package com.restaurant.user.service;

import com.restaurant.user.controller.dto.*;
import com.restaurant.user.service.dto.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByEmail(String email);

    List<User> getAll();

    boolean existsByUserId(Long userId);

    User insert(UserRequest userRequest);

    Optional<User> changeUserDetails(String email, UpdateUserRequest updateUserRequest);

    boolean existsUserByEmail(String email);

    boolean verifyAndLogUser(LoginRequest loginRequest);

    void logout(String token);

    void deleteByEmail(String email);

    void deleteUser(Long userId);

    boolean verifyPassword(String email, String currentPassword);

    String generateToken(String email);

    boolean changeUserPassword(Long userId, UserChangePasswordRequest userChangePasswordRequest);

    boolean changePassword(String email, ChangePasswordRequest changePasswordRequest);

    boolean isValidEmail(String email);

    void completeOrder(Long userId, Double cartValue);

    void updateUserBalance(Long userId, UserMoney userMoney);
}
