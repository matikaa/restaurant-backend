package com.restaurant.user.repository.dto;

import com.restaurant.user.controller.dto.UpdateUserRequest;

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
        Double money,
        Boolean loyaltyCard
) {

    public UserModel newUser(String encodedPassword) {
        return new UserModel(
                userId,
                email,
                name,
                encodedPassword,
                LocalDateTime.now().withNano(0),
                "USER",
                address,
                phoneNumber,
                0D,
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

    public UserModel modifiedUser(UpdateUserRequest updateUserRequest) {
        return new UserModel(
                userId,
                email,
                updateUserRequest.name(),
                password,
                createdTime,
                role,
                updateUserRequest.address(),
                updateUserRequest.phoneNumber(),
                money,
                loyaltyCard
        );
    }
}
