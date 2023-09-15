package com.restaurant.app.user.service.dto;

import java.time.LocalDateTime;

public record User(
        Long userId,
        String email,
        String name,
        LocalDateTime createdTime,
        String role,
        String address,
        String phoneNumber,
        Long money,
        Boolean loyaltyCard
) {
}
