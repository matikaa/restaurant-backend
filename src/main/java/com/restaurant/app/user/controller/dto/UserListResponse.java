package com.restaurant.app.user.controller.dto;

import java.util.List;

public record UserListResponse(
        List<UserResponse> userResponses
) {
}
