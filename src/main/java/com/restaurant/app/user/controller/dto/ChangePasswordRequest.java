package com.restaurant.app.user.controller.dto;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}
