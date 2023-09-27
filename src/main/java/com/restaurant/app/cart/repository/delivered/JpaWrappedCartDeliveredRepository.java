package com.restaurant.app.cart.repository.delivered;

import com.restaurant.app.cart.repository.current.dto.CartModel;
import com.restaurant.app.cart.repository.delivered.dto.CartDeliveredModel;

import java.util.List;
import java.util.Optional;

public class JpaWrappedCartDeliveredRepository implements CartDeliveredRepository {

    private static final CartDeliveredRepositoryMapper cartDeliveredRepositoryMapper = CartDeliveredRepositoryMapper.INSTANCE;

    private final CartDeliveredJpaRepository cartDeliveredJpaRepository;

    public JpaWrappedCartDeliveredRepository(CartDeliveredJpaRepository cartDeliveredJpaRepository) {
        this.cartDeliveredJpaRepository = cartDeliveredJpaRepository;
    }

    @Override
    public void insert(Optional<CartModel> cartModel) {
        cartModel.ifPresent(model -> cartDeliveredJpaRepository.save(
                cartDeliveredRepositoryMapper.cartModelToCartDeliveredEntity(model)
        ));
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return cartDeliveredJpaRepository.existsByUserId(userId);
    }

    @Override
    public List<CartDeliveredModel> findCartsDeliveredByUserId(Long userId) {
        return cartDeliveredRepositoryMapper.cartDeliveredEntitiesToCartDeliveredModels(
                cartDeliveredJpaRepository.findCartDeliveredEntitiesByUserId(userId));
    }
}
