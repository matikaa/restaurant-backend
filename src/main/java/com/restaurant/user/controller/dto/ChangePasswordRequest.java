package com.restaurant.user.controller.dto;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}
