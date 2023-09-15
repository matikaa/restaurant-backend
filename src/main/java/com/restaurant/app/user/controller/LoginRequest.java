package com.restaurant.app.user.controller;

public record LoginRequest(
        String email,
        String password
) {
}
