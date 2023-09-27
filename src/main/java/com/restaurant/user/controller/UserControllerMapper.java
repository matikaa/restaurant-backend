package com.restaurant.user.controller;

import com.restaurant.user.controller.dto.LoginRequestResponse;
import com.restaurant.user.controller.dto.UserRequestResponse;
import com.restaurant.user.controller.dto.UserResponse;
import com.restaurant.user.service.dto.User;
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
