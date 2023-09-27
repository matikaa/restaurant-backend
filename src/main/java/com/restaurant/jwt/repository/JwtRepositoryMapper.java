package com.restaurant.jwt.repository;

import com.restaurant.jwt.repository.dto.JwtModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JwtRepositoryMapper {

    JwtRepositoryMapper INSTANCE = Mappers.getMapper(JwtRepositoryMapper.class);

    JwtModel jwtEntityToJwtModel(JwtEntity jwtEntity);

    JwtEntity jwtModelToJwtEntity(JwtModel jwtModel);
}
