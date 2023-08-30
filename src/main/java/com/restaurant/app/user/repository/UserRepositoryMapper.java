package com.restaurant.app.user.repository;

import com.restaurant.app.category.repository.entity.UserPassword;
import com.restaurant.app.user.repository.dto.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserRepositoryMapper {

    UserRepositoryMapper INSTANCE = Mappers.getMapper(UserRepositoryMapper.class);

    List<UserModel> userEntitiesToUserModels(List<UserEntity> userEntity);

    UserModel userEntityToUserModel(UserEntity userEntity);

    UserEntity userModelToUserEntity(UserModel userModel);

    UserPassword userModelToUserPassword(UserModel userModel);
}
