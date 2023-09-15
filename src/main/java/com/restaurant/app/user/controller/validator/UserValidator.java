package com.restaurant.app.user.controller.validator;

import com.restaurant.app.user.controller.LoginRequest;
import com.restaurant.app.user.controller.dto.ChangePasswordRequest;
import com.restaurant.app.user.controller.dto.UserRequest;

public class UserValidator {

    public boolean isUserRequestNotValid(UserRequest userRequest) {
        return !(userRequest instanceof UserRequest) || userRequest.name() == null ||
                userRequest.name().trim().isEmpty() || userRequest.email() == null ||
                userRequest.email().trim().isEmpty() || userRequest.password() == null ||
                userRequest.password().trim().isEmpty();
    }

    public boolean isLoginRequestNotValid(LoginRequest loginRequest) {
        return !(loginRequest instanceof LoginRequest) || loginRequest.email() == null ||
                loginRequest.email().trim().isEmpty() || loginRequest.password() == null ||
                loginRequest.password().trim().isEmpty();
    }

    public boolean isStringNotValid(String header) {
        return header == null || header.trim().isEmpty();
    }

    public boolean isChangePasswordRequestNotValid(ChangePasswordRequest changePasswordRequest) {
        return !(changePasswordRequest instanceof ChangePasswordRequest) ||
                changePasswordRequest.currentPassword() == null ||
                changePasswordRequest.currentPassword().trim().isEmpty() ||
                changePasswordRequest.newPassword() == null || changePasswordRequest.newPassword().trim().isEmpty();
    }
}
