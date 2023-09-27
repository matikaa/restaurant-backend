package com.restaurant.user.controller.dto;

import java.util.List;

public record UserListResponse(
        List<UserResponse> userResponses
) {
}
