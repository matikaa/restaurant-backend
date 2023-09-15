package com.restaurant.app.user.repository;

import com.restaurant.app.user.repository.dto.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<UserModel> findById(Long userId);

    List<UserModel> findAll();

    UserModel save(UserModel userRequest);

    boolean existsByEmail(String email);

    String getCurrentPassword(String email);

    void changePassword(String email, String newPassword);

    boolean existsByUserId(Long userId);
}
