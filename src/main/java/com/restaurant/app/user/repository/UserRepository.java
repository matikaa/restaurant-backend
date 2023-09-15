package com.restaurant.app.user.repository;

import com.restaurant.app.user.controller.dto.UpdateUserRequest;
import com.restaurant.app.user.repository.dto.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<UserModel> findById(Long userId);

    List<UserModel> findAll();

    UserModel save(UserModel userRequest);

    Optional<UserModel> changeUser(String email, UpdateUserRequest updateUserRequest);

    boolean existsByEmail(String email);

    String getCurrentPassword(String email);

    void changePassword(String email, String newPassword);

    void deleteByUserEmail(String email);

    void deleteById(Long userId);

    boolean existsByUserId(Long userId);
}
