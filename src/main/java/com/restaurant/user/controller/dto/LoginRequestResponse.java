package com.restaurant.user.controller.dto;

public record LoginRequestResponse(
        Long userId,
        String token
) {
}
