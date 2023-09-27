package com.restaurant.app.user.controller.dto;

public record LoginRequest(
        String email,
        String password
) {
}
