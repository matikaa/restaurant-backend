package com.restaurant.user.controller.dto;

public record UserRequest(
        String email,
        String name,
        String password,
        String address,
        String phoneNumber
) {
}
