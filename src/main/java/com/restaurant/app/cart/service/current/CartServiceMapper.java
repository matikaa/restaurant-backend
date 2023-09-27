package com.restaurant.app.cart.service.current;

import com.restaurant.app.cart.repository.current.dto.CartModel;
import com.restaurant.app.cart.service.current.dto.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface CartServiceMapper {

    CartServiceMapper INSTANCE = Mappers.getMapper(CartServiceMapper.class);

    default Optional<Cart> cartModelToCart(Optional<CartModel> cartModel) {
        return cartModel.map(this::mapCartModelToCart);
    }

    Cart mapCartModelToCart(CartModel cartModel);
}
