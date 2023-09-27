package com.restaurant.app.cart.repository.delivered;

import com.restaurant.app.cart.repository.current.dto.CartModel;
import com.restaurant.app.cart.repository.delivered.dto.CartDeliveredModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartDeliveredRepositoryMapper {

    CartDeliveredRepositoryMapper INSTANCE = Mappers.getMapper(CartDeliveredRepositoryMapper.class);

    CartDeliveredEntity cartModelToCartDeliveredEntity(CartModel cartModel);

    default List<CartDeliveredModel> cartDeliveredEntitiesToCartDeliveredModels(List<CartDeliveredEntity> cartDeliveredEntities) {
        return cartDeliveredEntities.stream()
                .map(this::cartDeliveredEntityToCartDeliveredModel)
                .toList();
    }

    CartDeliveredModel cartDeliveredEntityToCartDeliveredModel(CartDeliveredEntity cartDeliveredEntity);
}
