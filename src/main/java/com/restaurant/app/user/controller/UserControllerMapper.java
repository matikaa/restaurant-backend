package com.restaurant.app.user.controller;

import com.restaurant.app.user.controller.dto.LoginRequestResponse;
import com.restaurant.app.user.controller.dto.UserRequestResponse;
import com.restaurant.app.user.controller.dto.UserResponse;
import com.restaurant.app.user.service.dto.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserControllerMapper {

    UserControllerMapper INSTANCE = Mappers.getMapper(UserControllerMapper.class);

    List<UserResponse> usersToUserResponses(List<User> users);

    UserResponse userToUserResponse(User user);

    UserRequestResponse userToUserRequestResponse(User user);

    LoginRequestResponse stringToLoginRequestResponse(String token);
}
