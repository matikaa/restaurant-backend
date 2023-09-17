package com.restaurant.app.cart.service.delivered;

import com.restaurant.app.cart.repository.current.dto.CartModel;
import com.restaurant.app.cart.repository.delivered.CartDeliveredRepository;
import com.restaurant.app.cart.repository.delivered.dto.CartDeliveredModel;
import com.restaurant.app.cart.service.delivered.dto.CartDelivered;

import java.util.List;
import java.util.Optional;

public class BaseCartDeliveredService implements CartDeliveredService {

    private static final CartDeliveredServiceMapper cartDeliveredServiceMapper = CartDeliveredServiceMapper.INSTANCE;

    private final CartDeliveredRepository cartDeliveredRepository;

    public BaseCartDeliveredService(CartDeliveredRepository cartDeliveredRepository) {
        this.cartDeliveredRepository = cartDeliveredRepository;
    }

    @Override
    public void addToUser(Optional<CartModel> cartModel) {
        cartDeliveredRepository.insert(cartModel);
    }

    @Override
    public List<CartDelivered> findByUserId(Long userId) {
        return cartDeliveredServiceMapper.cartDeliveredModelsToCartDelivered(
                cartDeliveredRepository.findCartsDeliveredByUserId(userId));
    }

    @Override
    public Double sumAll(Long userId) {
        return cartDeliveredRepository.findCartsDeliveredByUserId(userId)
                .stream()
                .mapToDouble(CartDeliveredModel::cartValue)
                .sum();
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return cartDeliveredRepository.existsByUserId(userId);
    }
}
