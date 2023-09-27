package com.restaurant.cart.service.current;

import com.restaurant.cart.repository.current.CartRepository;
import com.restaurant.cart.service.current.dto.Cart;
import com.restaurant.cart.service.delivered.CartDeliveredService;
import com.restaurant.cart.service.delivered.dto.CartDelivered;
import com.restaurant.common.ConstantValues;
import com.restaurant.common.Status;
import com.restaurant.food.service.dto.Food;
import com.restaurant.user.service.dto.User;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class BaseCartService implements CartService {

    private static final CartServiceMapper cartServiceMapper = CartServiceMapper.INSTANCE;

    private final CartRepository cartRepository;

    private final CartDeliveredService cartDeliveredService;

    public BaseCartService(CartRepository cartRepository, CartDeliveredService cartDeliveredService) {
        this.cartRepository = cartRepository;
        this.cartDeliveredService = cartDeliveredService;
    }

    @Override
    public Optional<Cart> addFoodToCart(User user, Food food) {
        Double cartValue = 10D;
        var foodPrice = food.foodPrice();

        if (cartRepository.existsByUserId(user.userId())) {
            cartValue = cartRepository.findValueOfCartByUserId(user.userId());
        } else {
            cartRepository.insert(user.userId());
        }

        if (user.money() >= sumAllExpenses(cartValue, foodPrice, user.loyaltyCard())) {
            if (user.loyaltyCard()) {
                return cartServiceMapper.cartModelToCart(
                        cartRepository.addToCart(
                                user.userId(), foodPrice - (foodPrice * ConstantValues.DISCOUNT), food, user.loyaltyCard()));
            }

            return cartServiceMapper.cartModelToCart(
                    cartRepository.addToCart(user.userId(), foodPrice, food, user.loyaltyCard()));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Cart> getCart(Long userId) {
        if (getOrderStatus(userId).equals(Status.IN_ORDER)) {
            return cartServiceMapper.cartModelToCart(cartRepository.findCartByUserId(userId));
        }

        return Optional.empty();
    }

    @Override
    public void changeStatus(Long userId) {
        cartRepository.changeStatusToInDelivery(userId);
    }

    @Override
    public Status confirmAnOrder(Long userId) {
        var status = getOrderStatus(userId);

        if (status.equals(Status.IN_DELIVERY)) {
            cartDeliveredService.addToUser(cartRepository.findCartByUserId(userId));
            cartRepository.confirmAnOrder(userId);
        }

        return status;
    }

    @Override
    public Status getOrderStatus(Long userId) {
        if (cartRepository.existsByUserId(userId)) {
            return cartRepository.getStatus(userId);
        }

        return Status.EMPTY_ORDER;
    }

    @Override
    public List<CartDelivered> getAllDeliveredCart(Long userId) {
        if (cartDeliveredService.existsByUserId(userId)) {
            return cartDeliveredService.findByUserId(userId);
        }

        return emptyList();
    }

    @Override
    public Double getOverallCartValue(Long userId) {
        if (cartDeliveredService.existsByUserId(userId)) {
            return ConstantValues.my_format(cartDeliveredService.sumAll(userId));
        }

        return 0D;
    }

    @Override
    public Status cancelOrder(Long userId) {
        var status = getStatus(userId);

        if (status.equals(Status.IN_ORDER)) {
            cartRepository.deleteOrder(userId);
        }

        return status;
    }

    @Override
    public void cancelSpecificOrder(Long userId, Food food) {
        cartRepository.deleteSpecificOrder(userId, food);
    }

    @Override
    public boolean existsFoodByName(Long userId, String foodName) {
        return cartRepository.existsFoodInOrder(userId, foodName);
    }

    private Double sumAllExpenses(Double cartValue, Double foodValue, Boolean loyaltyCard) {
        if (loyaltyCard) {
            return cartValue + (foodValue - (foodValue * ConstantValues.DISCOUNT));
        }

        return cartValue + foodValue;
    }

    private Status getStatus(Long userId) {
        if (cartRepository.existsByUserId(userId)) {
            if (getOrderStatus(userId).equals(Status.IN_ORDER)) {

                return Status.IN_ORDER;
            }

            return Status.IN_DELIVERY;
        }

        return Status.EMPTY_ORDER;
    }
}
