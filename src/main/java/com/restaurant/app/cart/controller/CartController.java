package com.restaurant.app.cart.controller;

import com.restaurant.app.cart.controller.dto.CartDeliveredResponse;
import com.restaurant.app.cart.controller.dto.CartResponse;
import com.restaurant.app.cart.service.current.CartService;
import com.restaurant.app.cart.service.current.dto.Cart;
import com.restaurant.app.common.Status;
import com.restaurant.app.food.service.FoodService;
import com.restaurant.app.food.service.dto.Food;
import com.restaurant.app.user.service.UserService;
import com.restaurant.app.user.service.dto.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.restaurant.app.common.ConstantValues.*;
import static org.springframework.http.HttpStatus.*;

@RestController
public class CartController {

    private static final CartControllerMapper cartControllerMapper = CartControllerMapper.INSTANCE;

    private final CartService cartService;

    private final UserService userService;

    private final FoodService foodService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    public CartController(CartService cartService, UserService userService, FoodService foodService) {
        this.cartService = cartService;
        this.userService = userService;
        this.foodService = foodService;
    }

    @PutMapping("/categories/{categoryId}/food/{foodId}/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> addToOrder(@PathVariable Long categoryId, @PathVariable Long foodId) {
        var user = getUser();
        if (user.isEmpty()) {
            LOGGER.warn(NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        if (!foodService.existsByCategoryIdAndFoodId(categoryId, foodId)) {
            LOGGER.warn(FOOD_WITH_CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        if (cartService.getOrderStatus(user.get().userId()).equals(Status.IN_DELIVERY)) {
            LOGGER.warn(ORDER_IN_DELIVERY);
            return ResponseEntity.status(CONFLICT).build();
        }

        var food = getFood(categoryId, foodId);

        return cartService.addFoodToCart(user.get(), food)
                .map(cartControllerMapper::cartToCartResponse)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> {
                    LOGGER.warn(NOT_ENOUGH_MONEY);
                    return ResponseEntity.status(PAYMENT_REQUIRED).build();
                });
    }

    @PutMapping("/users/{userId}/cart/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> confirmOrder(@PathVariable Long userId) {
        var user = getUser();
        if (user.isEmpty()) {
            LOGGER.warn(NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        var cart = cartService.getCart(userId);
        if (cart.isEmpty()) {
            LOGGER.warn(EMPTY_CART);
            return ResponseEntity.notFound().build();
        }

        userService.completeOrder(userId, getcartValue(cart));
        cartService.changeStatus(userId);

        return ResponseEntity.ok().body(
                cartControllerMapper.cartToCartResponse(cart.get())
        );
    }

    @PostMapping("/users/{userId}/cart/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Void> confirmDelivery(@PathVariable Long userId) {
        var user = getUser();
        if (user.isEmpty()) {
            LOGGER.warn(NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        if (!cartService.confirmAnOrder(userId).equals(Status.IN_DELIVERY)) {
            LOGGER.warn(INVALID_DELIVERY);
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/cart/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> getCurrentUserOrder(@PathVariable Long userId) {
        var user = userService.getUserById(userId);
        if (user.isEmpty()) {
            LOGGER.warn(NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        return cartService.getCart(userId)
                .map(cartControllerMapper::cartToCartResponse)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> {
                    LOGGER.warn(EMPTY_CART);
                    return ResponseEntity.status(CONFLICT).build();
                });
    }

    @GetMapping("/users/{userId}/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<CartDeliveredResponse> getAllUserOrders(@PathVariable Long userId) {
        var user = userService.getUserById(userId);
        if (user.isEmpty()) {
            LOGGER.warn(NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        return ResponseEntity.ok().body(
                new CartDeliveredResponse(cartService.getOverallCartValue(userId),
                        cartControllerMapper.cartDeliveredToCartDeliveredResponse(
                                cartService.getAllDeliveredCart(userId)).stream().toList()));
    }

    @DeleteMapping("/users/{userId}/cart/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Transactional
    public ResponseEntity<Void> cancelOrder(@PathVariable Long userId) {
        var user = userService.getUserById(userId);
        if (user.isEmpty()) {
            LOGGER.warn(NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        if (!cartService.cancelOrder(userId).equals(Status.IN_ORDER)) {
            LOGGER.warn(INVALID_CANCEL);
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/categories/{categoryId}/food/{foodId}/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteFromOrder(@PathVariable Long categoryId, @PathVariable Long foodId) {
        var user = getUser();
        if (user.isEmpty()) {
            LOGGER.warn(NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        var userId = user.get().userId();

        if (!foodService.existsByCategoryIdAndFoodId(categoryId, foodId)) {
            LOGGER.warn(FOOD_WITH_CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        if (cartService.getOrderStatus(userId).equals(Status.EMPTY_ORDER)) {
            LOGGER.warn(EMPTY_CART);
            return ResponseEntity.status(CONFLICT).build();
        }

        var food = foodService.getFoodByCategoryIdAndFoodId(categoryId, foodId).get();

        if (!cartService.existsFoodByName(userId, food.foodName())) {
            LOGGER.warn(ORDER_WITHOUT_FOOD);
            return ResponseEntity.badRequest().build();
        }

        cartService.cancelSpecificOrder(userId, food);

        return ResponseEntity.ok().build();
    }

    private Optional<User> getUser() {
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userService.existsUserByEmail(userEmail)) {
            return Optional.empty();
        }

        return userService.getUserByEmail(userEmail);
    }

    private Double getcartValue(Optional<Cart> cartModel) {
        return my_format(cartModel.get().cartValue());
    }

    private Food getFood(Long categoryId, Long foodId) {
        return foodService.getFoodByCategoryIdAndFoodId(categoryId, foodId).get();
    }
}
