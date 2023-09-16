package com.restaurant.app.user.controller.dto;

public record UpdateUserRequest(
        String name,
        String address,
        String phoneNumber
) {
}
