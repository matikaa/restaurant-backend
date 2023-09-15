package com.restaurant.app.user.controller.validator;

import com.restaurant.app.user.controller.LoginRequest;
import com.restaurant.app.user.controller.dto.*;

public class UserValidator {

    public boolean isUserRequestNotValid(UserRequest userRequest) {
        return !(userRequest instanceof UserRequest) || userRequest.name() == null ||
                userRequest.name().trim().isEmpty() || userRequest.email() == null ||
                userRequest.email().trim().isEmpty() || userRequest.password() == null ||
                userRequest.password().trim().isEmpty() || userRequest.address() == null ||
                userRequest.address().trim().isEmpty() || userRequest.phoneNumber() == null ||
                userRequest.phoneNumber().trim().isEmpty();
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

    public boolean isUpdateUserRequestNotValid(UpdateUserRequest updateUserRequest) {
        return !(updateUserRequest instanceof UpdateUserRequest) ||
                updateUserRequest.name() == null || updateUserRequest.name().trim().isEmpty() ||
                updateUserRequest.address() == null || updateUserRequest.address().trim().isEmpty() ||
                updateUserRequest.phoneNumber() == null || updateUserRequest.phoneNumber().trim().isEmpty();
    }

    public boolean isPasswordRequestNotValid(PasswordRequest passwordRequest) {
        return !(passwordRequest instanceof PasswordRequest) ||
                passwordRequest.password() == null || passwordRequest.password().trim().isEmpty();
    }

    public boolean isUserChangePasswordRequestNotValid(UserChangePasswordRequest userChangePasswordRequest) {
        return !(userChangePasswordRequest instanceof UserChangePasswordRequest) ||
                userChangePasswordRequest.newPassword() == null ||
                userChangePasswordRequest.newPassword().trim().isEmpty();
    }
}
