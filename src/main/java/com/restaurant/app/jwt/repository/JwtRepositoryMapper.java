package com.restaurant.app.jwt.repository;

import com.restaurant.app.jwt.repository.dto.JwtModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JwtRepositoryMapper {

    JwtRepositoryMapper INSTANCE = Mappers.getMapper(JwtRepositoryMapper.class);

    JwtModel jwtEntityToJwtModel(JwtEntity jwtEntity);

    JwtEntity jwtModelToJwtEntity(JwtModel jwtModel);
}
