package com.restaurant.user.controller.dto;

public record LoginRequest(
        String email,
        String password
) {
}
