package com.restaurant.cart.repository.delivered;

import com.restaurant.cart.repository.current.dto.CartModel;
import com.restaurant.cart.repository.delivered.dto.CartDeliveredModel;

import java.time.ZonedDateTime;
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
        cartModel.ifPresent(model -> cartDeliveredJpaRepository.save(setOrderDate(
                cartDeliveredRepositoryMapper.cartModelToCartDeliveredEntity(model))
        ));
    }

    public CartDeliveredEntity setOrderDate(CartDeliveredEntity cartDeliveredEntity) {
        cartDeliveredEntity.setOrderDate(ZonedDateTime.now());
        return cartDeliveredEntity;
    }

    @Override
    public List<CartDeliveredEntity> findAll() {
        return cartDeliveredJpaRepository.findAll();
    }

    @Override
    public List<CartDeliveredModel> findAllOrders() {
        return cartDeliveredRepositoryMapper.cartDeliveredEntitiesToCartDeliveredModels(
                cartDeliveredJpaRepository.findAll());
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
