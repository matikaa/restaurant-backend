package com.restaurant.app.user.repository.dto;

import java.time.LocalDateTime;

public record UserModel(
        Long userId,
        String email,
        String name,
        String password,
        LocalDateTime createdTime,
        String role,
        String address,
        String phoneNumber,
        Long money,
        Boolean loyaltyCard
) {

    public UserModel modifiedUser(String encodedPassword) {
        return new UserModel(
                userId,
                email,
                name,
                encodedPassword,
                LocalDateTime.now().withNano(0),
                "USER",
                address,
                phoneNumber,
                0L,
                false
        );
    }

    public UserModel userWithNewPassword(String newPassword) {
        return new UserModel(
                this.userId,
                this.email,
                this.name,
                newPassword,
                this.createdTime,
                this.role,
                address,
                phoneNumber,
                money,
                loyaltyCard
        );
    }
}
