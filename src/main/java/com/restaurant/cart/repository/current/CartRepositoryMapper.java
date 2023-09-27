package com.restaurant.cart.repository.current;

import com.restaurant.cart.repository.current.dto.CartModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartRepositoryMapper {

    CartRepositoryMapper INSTANCE = Mappers.getMapper(CartRepositoryMapper.class);

    CartModel cartEntityToCartModel(CartEntity cartEntity);
}
