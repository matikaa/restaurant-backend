package com.restaurant.user.controller;

import com.restaurant.user.controller.dto.*;
import com.restaurant.user.service.dto.User;
import com.restaurant.user.service.dto.UserLogin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserControllerMapper {

    UserControllerMapper INSTANCE = Mappers.getMapper(UserControllerMapper.class);

    List<UserResponse> usersToUserResponses(List<User> users);

    UserResponse userToUserResponse(User user);

    UserRole userToUserRole(User user);

    UserMoney userToUserMoney(User user);

    UserRequestResponse userToUserRequestResponse(User user);

    LoginRequestResponse userLoginToLoginRequestResponse(UserLogin userLogin);
}
