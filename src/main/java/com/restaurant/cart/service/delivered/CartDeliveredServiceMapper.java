package com.restaurant.cart.service.delivered;

import com.restaurant.cart.repository.delivered.dto.CartDeliveredModel;
import com.restaurant.cart.service.delivered.dto.CartDelivered;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartDeliveredServiceMapper {

    CartDeliveredServiceMapper INSTANCE = Mappers.getMapper(CartDeliveredServiceMapper.class);

    default List<CartDelivered> cartDeliveredModelsToCartDelivered(List<CartDeliveredModel> cartDeliveredModels) {
        return cartDeliveredModels.stream()
                .map(this::cartDeliveredModelToCartDelivered)
                .toList();
    }

    CartDelivered cartDeliveredModelToCartDelivered(CartDeliveredModel cartDeliveredModel);
}
