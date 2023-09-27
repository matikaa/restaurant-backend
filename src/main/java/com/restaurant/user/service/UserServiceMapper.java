package com.restaurant.user.service;

import com.restaurant.user.controller.dto.UserRequest;
import com.restaurant.user.repository.dto.UserModel;
import com.restaurant.user.service.dto.User;
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
