package com.restaurant.app.user.service;

import com.restaurant.app.user.controller.dto.UserRequest;
import com.restaurant.app.user.repository.dto.UserModel;
import com.restaurant.app.user.service.dto.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserServiceMapper {

    UserServiceMapper INSTANCE = Mappers.getMapper(UserServiceMapper.class);

    List<User> userModelsToUsers(List<UserModel> userModels);

    User userModelToUser(UserModel userModel);

    UserModel userRequestToUserModel(UserRequest userRequest);
}
