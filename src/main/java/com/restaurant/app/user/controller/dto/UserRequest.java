package com.restaurant.app.user.controller.dto;

public record UserRequest(
        String email,
        String name,
        String password,
        String address,
        String phoneNumber
) {
}
