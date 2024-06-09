package com.restaurant.cart.repository.delivered;

import com.restaurant.cart.repository.current.dto.CartModel;
import com.restaurant.cart.repository.delivered.dto.CartDeliveredModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;
import java.util.List;

@Mapper
public interface CartDeliveredRepositoryMapper {

    CartDeliveredRepositoryMapper INSTANCE = Mappers.getMapper(CartDeliveredRepositoryMapper.class);

    default CartDeliveredEntity cartModelToCartDeliveredEntity(CartModel cartModel) {
        return new CartDeliveredEntity(
                cartModel.userId(),
                cartModel.loyaltyCard(),
                cartModel.cartValue(),
                cartModel.food(),
                cartModel.foodPrice(),
                ZonedDateTime.now()
        );
    }

    default List<CartDeliveredModel> cartDeliveredEntitiesToCartDeliveredModels(List<CartDeliveredEntity> cartDeliveredEntities) {
        return cartDeliveredEntities.stream()
                .map(this::cartDeliveredEntityToCartDeliveredModel)
                .toList();
    }

    CartDeliveredModel cartDeliveredEntityToCartDeliveredModel(CartDeliveredEntity cartDeliveredEntity);
}
