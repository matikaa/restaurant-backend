package com.restaurant.cart.repository.current;

import com.restaurant.cart.repository.current.dto.CartModel;
import com.restaurant.common.ConstantValues;
import com.restaurant.common.Status;
import com.restaurant.food.service.dto.Food;

import java.util.List;
import java.util.Optional;

public class JpaWrappedCartRepository implements CartRepository {

    private static final CartRepositoryMapper cartRepositoryMapper = CartRepositoryMapper.INSTANCE;

    private final CartJpaRepository cartJpaRepository;

    public JpaWrappedCartRepository(CartJpaRepository cartJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
    }

    @Override
    public Double findValueOfCartByUserId(Long userId) {
        return findCartByUserId(userId).get().cartValue();
    }

    @Override
    public Optional<CartModel> findCartByUserId(Long userId) {
        return cartJpaRepository.findCartEntityByUserId(userId)
                .map(cartRepositoryMapper::cartEntityToCartModel);
    }

    @Override
    public Optional<CartModel> addToCart(Long userId, Double foodValue, Food food, Boolean loyaltyCard) {
        return cartJpaRepository.findCartEntityByUserId(userId)
                .map(cart -> updateCart(foodValue, food, cart, loyaltyCard))
                .map(cartJpaRepository::save)
                .map(cartRepositoryMapper::cartEntityToCartModel);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return cartJpaRepository.existsByUserId(userId);
    }

    @Override
    public void insert(Long userId) {
        var cartEntity = getCartEntity(userId);
        cartJpaRepository.save(cartEntity);
    }

    @Override
    public void confirmAnOrder(Long userId) {
        cartJpaRepository.findCartEntityByUserId(userId)
                .ifPresent(cartJpaRepository::delete);
    }

    @Override
    public void changeStatusToInDelivery(Long userId) {
        cartJpaRepository.findCartEntityByUserId(userId)
                .map(this::changeByMakeAnOrder)
                .map(cartJpaRepository::save)
                .map(cartRepositoryMapper::cartEntityToCartModel);
    }

    @Override
    public Status getStatus(Long userId) {
        var cart = cartJpaRepository.findCartEntityByUserId(userId);
        if (cart.isPresent()) {
            return cart.get().getStatus();
        }

        return Status.EMPTY_ORDER;
    }

    @Override
    public void deleteOrder(Long userId) {
        cartJpaRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteSpecificOrder(Long userId, Food food) {
        cartJpaRepository.findCartEntityByUserId(userId)
                .map(cart -> deleteSpecificFood(cart, food))
                .map(cartJpaRepository::save);
    }

    @Override
    public boolean existsFoodInOrder(Long userId, String foodName) {
        if (findCartByUserId(userId).get()
                .food().stream()
                .filter(name -> name.equals(foodName)).findAny()
                .isPresent()) {

            return true;
        }

        return false;
    }

    private CartEntity deleteSpecificFood(CartEntity cartEntity, Food food) {
        var index = cartEntity.getFood().indexOf(food.foodName());
        var foodPrice = cartEntity.getFoodPrice().get(index);
        Double value;

        if (cartEntity.getLoyaltyCard()) {
            value = foodPrice - foodPrice * ConstantValues.DISCOUNT;
        } else {
            value = foodPrice;
        }

        var cartValue = cartEntity.getCartValue();

        cartEntity.setCartValue(ConstantValues.my_format(cartValue - value));
        cartEntity.getFood().remove(index);
        cartEntity.getFoodPrice().remove(index);

        return cartEntity;
    }

    private CartEntity updateCart(Double foodValue, Food food, CartEntity cartEntity, Boolean loyaltyCard) {
        var value = cartEntity.getCartValue();
        cartEntity.setCartValue(ConstantValues.my_format(value + foodValue));
        cartEntity.setLoyaltyCard(loyaltyCard);

        List<String> foodList = cartEntity.getFood();
        foodList.add(food.foodName());
        cartEntity.setFood(foodList);

        List<Double> foodPriceList = cartEntity.getFoodPrice();
        foodPriceList.add(ConstantValues.my_format(food.foodPrice()));
        cartEntity.setFoodPrice(foodPriceList);

        return cartEntity;
    }

    private CartEntity changeByMakeAnOrder(CartEntity cartEntity) {
        cartEntity.setStatus(Status.IN_DELIVERY);

        return cartEntity;
    }

    private CartEntity getCartEntity(Long userId) {
        var entity = new CartEntity();
        entity.setUserId(userId);
        entity.setCartValue(ConstantValues.my_format(ConstantValues.DELIVERY_PRICE));
        entity.setStatus(Status.IN_ORDER);

        return entity;
    }

}
