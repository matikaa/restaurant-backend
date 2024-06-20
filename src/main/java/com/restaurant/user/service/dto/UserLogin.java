package com.restaurant.user.service.dto;

public record UserLogin(
        Long userId,
        String token
) {
}
