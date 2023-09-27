package com.restaurant.user.service.dto;

import java.time.LocalDateTime;

public record User(
        Long userId,
        String email,
        String name,
        LocalDateTime createdTime,
        String role,
        String address,
        String phoneNumber,
        Double money,
        Boolean loyaltyCard
) {
}
