package com.restaurant.user.controller.dto;

import java.time.LocalDateTime;

public record UserRequestResponse(
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
