package com.restaurant.cart.controller;

import com.restaurant.cart.controller.dto.CartDeliveredResponse;
import com.restaurant.cart.controller.dto.CartResponse;
import com.restaurant.cart.controller.dto.CartStatisticResponse;
import com.restaurant.cart.controller.dto.OrderDate;
import com.restaurant.cart.service.current.CartService;
import com.restaurant.cart.service.current.dto.Cart;
import com.restaurant.common.ConstantValues;
import com.restaurant.common.Status;
import com.restaurant.food.service.FoodService;
import com.restaurant.food.service.dto.Food;
import com.restaurant.user.service.UserService;
import com.restaurant.user.service.dto.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        if (!foodService.existsByCategoryIdAndFoodId(categoryId, foodId)) {
            LOGGER.warn(ConstantValues.FOOD_WITH_CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        if (cartService.getOrderStatus(user.get().userId()).equals(Status.IN_DELIVERY)) {
            LOGGER.warn(ConstantValues.ORDER_IN_DELIVERY);
            return ResponseEntity.status(CONFLICT).build();
        }

        var food = getFood(categoryId, foodId);

        return cartService.addFoodToCart(user.get(), food)
                .map(cartControllerMapper::cartToCartResponse)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> {
                    LOGGER.warn(ConstantValues.NOT_ENOUGH_MONEY);
                    return ResponseEntity.status(PAYMENT_REQUIRED).build();
                });
    }

    @PutMapping("/users/{userId}/cart/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> confirmOrder(@PathVariable Long userId) {
        var user = getUser();
        if (user.isEmpty()) {
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        var cart = cartService.getCart(userId);
        if (cart.isEmpty()) {
            LOGGER.warn(ConstantValues.EMPTY_CART);
            return ResponseEntity.notFound().build();
        }

        userService.completeOrder(userId, getCartValue(cart));
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
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        if (!cartService.confirmAnOrder(userId).equals(Status.IN_DELIVERY)) {
            LOGGER.warn(ConstantValues.INVALID_DELIVERY);
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/cart/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> getCurrentUserOrder(@PathVariable Long userId) {
        var user = userService.getUserById(userId);
        if (user.isEmpty()) {
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        return cartService.getCart(userId)
                .map(cartControllerMapper::cartToCartResponse)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> {
                    LOGGER.warn(ConstantValues.EMPTY_CART);
                    return ResponseEntity.status(CONFLICT).build();
                });
    }

    @GetMapping("/users/{userId}/cart/order_in_delivery")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> getInDeliveryUserOrder(@PathVariable Long userId) {
        var user = userService.getUserById(userId);
        if (user.isEmpty()) {
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        return cartService.getInDeliveryCart(userId)
                .map(cartControllerMapper::cartToCartResponse)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> {
                    LOGGER.warn(ConstantValues.EMPTY_CART);
                    return ResponseEntity.status(CONFLICT).build();
                });
    }

    @GetMapping("/users/{userId}/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<CartDeliveredResponse> getAllUserOrders(@PathVariable Long userId) {
        var user = userService.getUserById(userId);
        if (user.isEmpty()) {
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        return ResponseEntity.ok().body(
                new CartDeliveredResponse(cartService.getOverallCartValue(userId),
                        cartControllerMapper.cartDeliveredsToCartResponseDelivereds(
                                cartService.getAllDeliveredCart(userId)).stream().toList()));
    }

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CartStatisticResponse> getAllSortedAndSoldFood(@RequestBody OrderDate orderDate) {
        return ResponseEntity.ok().body(new CartStatisticResponse(
                cartService.getValueOfAllOrders(orderDate), cartControllerMapper.soldFoodSummaryToSoldFoodSummaryResponse(
                        cartService.getOverallSoldFood(orderDate))
        ));
    }

    @DeleteMapping("/users/{userId}/cart/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Transactional
    public ResponseEntity<Void> cancelOrder(@PathVariable Long userId) {
        var user = userService.getUserById(userId);
        if (user.isEmpty()) {
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        if (!cartService.cancelOrder(userId).equals(Status.IN_ORDER)) {
            LOGGER.warn(ConstantValues.INVALID_CANCEL);
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/categories/{categoryId}/food/{foodId}/order")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteFromOrder(@PathVariable Long categoryId, @PathVariable Long foodId) {
        var user = getUser();
        if (user.isEmpty()) {
            LOGGER.warn(ConstantValues.NO_ACCESS);
            return ResponseEntity.status(FORBIDDEN).build();
        }

        var userId = user.get().userId();

        if (!foodService.existsByCategoryIdAndFoodId(categoryId, foodId)) {
            LOGGER.warn(ConstantValues.FOOD_WITH_CATEGORY_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        if (!cartService.getOrderStatus(userId).equals(Status.IN_ORDER)) {
            LOGGER.warn(ConstantValues.EMPTY_CART);
            return ResponseEntity.status(CONFLICT).build();
        }

        var food = foodService.getFoodByCategoryIdAndFoodId(categoryId, foodId).get();

        if (!cartService.existsFoodByName(userId, food.foodName())) {
            LOGGER.warn(ConstantValues.ORDER_WITHOUT_FOOD);
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

    private Double getCartValue(Optional<Cart> cartModel) {
        return ConstantValues.my_format(cartModel.get().cartValue());
    }

    private Food getFood(Long categoryId, Long foodId) {
        return foodService.getFoodByCategoryIdAndFoodId(categoryId, foodId).get();
    }
}
