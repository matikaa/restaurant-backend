package com.restaurant.services.cart;

import com.restaurant.cart.repository.current.CartRepository;
import com.restaurant.cart.repository.current.dto.CartModel;
import com.restaurant.cart.repository.delivered.CartDeliveredRepository;
import com.restaurant.cart.service.current.BaseCartService;
import com.restaurant.cart.service.current.dto.Cart;
import com.restaurant.cart.service.delivered.BaseCartDeliveredService;
import com.restaurant.cart.service.delivered.CartDeliveredService;
import com.restaurant.cart.service.delivered.dto.CartDelivered;
import com.restaurant.common.Status;
import com.restaurant.food.service.dto.Food;
import com.restaurant.user.service.dto.User;
import com.restaurant.services.BaseTestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.restaurant.common.ConstantValues.DISCOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseCartServiceTest extends BaseTestUseCase {

    @InjectMocks
    private BaseCartService baseCartService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private BaseCartDeliveredService baseCartDeliveredService;

    @Mock
    private CartDeliveredRepository cartDeliveredRepository;

    @Mock
    private CartDeliveredService cartDeliveredService;

    @Test
    @DisplayName("Should get status of order")
    void shouldGetStatusOfOrder() {
        //given
        boolean expectedUserExists = true;
        User user = getWithUser();

        when(cartRepository.existsByUserId(user.userId())).thenReturn(expectedUserExists);
        when(cartRepository.getStatus(user.userId())).thenReturn(Status.EMPTY_ORDER);

        //when
        Status statusOfOrder = baseCartService.getOrderStatus(user.userId());

        //then
        assertEquals(Status.EMPTY_ORDER, statusOfOrder);
    }

    @Test
    @DisplayName("Should confirm an order")
    void shouldConfirmAnOrder() {
        //given
        boolean expectedUserExists = true;
        User user = getWithUser();

        when(cartRepository.existsByUserId(user.userId())).thenReturn(expectedUserExists);
        when(cartRepository.getStatus(user.userId())).thenReturn(Status.IN_ORDER);

        //when
        Status statusOfOrder = baseCartService.confirmAnOrder(user.userId());

        //then
        assertEquals(Status.IN_ORDER, statusOfOrder);
    }

    @Test
    @DisplayName("Should add food to cart with discount")
    void shouldAddFoodToCartWithDiscount() {
        //given
        Double valueOfCart = 40D;
        boolean expectedUserExists = true;

        User user = getWithUser();
        Food food = getFood();
        CartModel cartModel = getWithCartModel();
        Cart cart = getCartFromCartModel(cartModel);

        Double foodValue = food.foodPrice() - (food.foodPrice() * DISCOUNT);

        when(cartRepository.existsByUserId(anyLong())).thenReturn(expectedUserExists);
        when(cartRepository.findValueOfCartByUserId(anyLong())).thenReturn(valueOfCart);
        when(cartRepository.addToCart(user.userId(), foodValue, food, user.loyaltyCard()))
                .thenReturn(Optional.of(cartModel));

        //when
        Optional<Cart> resultOfAddedFoodToCart = baseCartService.addFoodToCart(user, food);

        //then
        assertEquals(Optional.of(cart), resultOfAddedFoodToCart);
    }

    @Test
    @DisplayName("Should add food to cart without discount")
    void shouldAddFoodToCartWithoutDiscount() {
        //given
        Double valueOfCart = 0D;
        boolean expectedUserExists = true;

        User user = getWithoutUser();
        Food food = getWithoutFood();
        CartModel cartModel = new CartModel(
                1L,
                1L,
                false,
                20D,
                List.of("Beer"),
                List.of(10D)
        );
        Cart cart = getCartFromCartModel(cartModel);

        when(cartRepository.existsByUserId(anyLong())).thenReturn(expectedUserExists);
        when(cartRepository.findValueOfCartByUserId(anyLong())).thenReturn(valueOfCart);
        when(cartRepository.addToCart(user.userId(), food.foodPrice(), food, user.loyaltyCard()))
                .thenReturn(Optional.of(cartModel));

        //when
        Optional<Cart> resultOfAddedFoodToCart = baseCartService.addFoodToCart(user, food);

        //then
        assertEquals(Optional.of(cart), resultOfAddedFoodToCart);
    }

    @Test
    @DisplayName("Should not add food to cart by not enough money")
    void shouldNotAddFoodToCartByNotEnoughMoney() {
        //given
        Double valueOfCart = 20D;
        boolean expectedUserExists = true;

        User user = getWithoutUser();
        Food food = getWithoutFood();

        when(cartRepository.existsByUserId(anyLong())).thenReturn(expectedUserExists);
        when(cartRepository.findValueOfCartByUserId(anyLong())).thenReturn(valueOfCart);

        //when
        Optional<Cart> resultOfAddedFoodToCart = baseCartService.addFoodToCart(user, food);

        //then
        assertEquals(Optional.empty(), resultOfAddedFoodToCart);
    }

    @Test
    @DisplayName("Should get cart by userId")
    void shouldGetCartByUserId() {
        //given
        boolean expectedUserExists = true;
        User user = getWithUser();
        CartModel cartModel = getWithCartModel();
        Cart cart = getCartFromCartModel(cartModel);

        when(cartRepository.existsByUserId(user.userId())).thenReturn(expectedUserExists);
        when(cartRepository.getStatus(user.userId())).thenReturn(Status.IN_ORDER);
        when(cartRepository.findCartByUserId(user.userId())).thenReturn(Optional.of(cartModel));

        //when
        Optional<Cart> resultOfAddedFoodToCart = baseCartService.getCart(user.userId());

        //then
        assertEquals(Optional.of(cart), resultOfAddedFoodToCart);
    }

    @Test
    @DisplayName("Should cancel order")
    void shouldCancelOrder() {
        //given
        boolean expectedUserExists = true;
        User user = getWithUser();

        when(cartRepository.existsByUserId(user.userId())).thenReturn(expectedUserExists);
        when(cartRepository.getStatus(user.userId())).thenReturn(Status.IN_ORDER);

        //when
        Status resultOfCancelOrder = baseCartService.cancelOrder(user.userId());

        //then
        assertEquals(Status.IN_ORDER, resultOfCancelOrder);
        verify(cartRepository).deleteOrder(user.userId());
    }

    @Test
    @DisplayName("Should cancel specific order")
    void shouldCancelSpecificOrder() {
        //given
        User user = getWithUser();
        Food food = getFood();

        //when
        baseCartService.cancelSpecificOrder(user.userId(), food);

        //then
        verify(cartRepository).deleteSpecificOrder(user.userId(), food);
    }

    @Test
    @DisplayName("Should check exists food by name")
    void shouldCheckExistsFoodByName() {
        //given
        boolean existsFood = true;
        User user = getWithUser();
        Food food = getFood();

        when(cartRepository.existsFoodInOrder(user.userId(), food.foodName())).thenReturn(existsFood);

        //when
        boolean exists = baseCartService.existsFoodByName(user.userId(), food.foodName());

        //then
        assertEquals(existsFood, exists);
    }

    @Test
    @DisplayName("Should get all delivered cart")
    void shouldGetAllDeliveredCart() {
        //given
        boolean expectedUserExists = true;
        User user = getWithUser();

        CartDelivered secCart = new CartDelivered(
                1L, 1L, true, 50D, List.of("Beer"), List.of(55D)
        );

        List<CartDelivered> carts = new ArrayList<>();
        carts.add(secCart);

        when(cartDeliveredService.existsByUserId(user.userId())).thenReturn(expectedUserExists);
        when(cartDeliveredService.findByUserId(user.userId())).thenReturn(carts);

        //when
        List<CartDelivered> resultOfAllCarts = baseCartService.getAllDeliveredCart(user.userId());

        //then
        assertEquals(carts, resultOfAllCarts);
    }

    @Test
    @DisplayName("Should get overall cart value")
    void shouldGetOverallCartValue() {
        //given
        boolean expectedUserExists = true;
        User user = getWithUser();
        Double value = 50D;

        when(cartDeliveredService.existsByUserId(user.userId())).thenReturn(expectedUserExists);
        when(cartDeliveredService.sumAll(user.userId())).thenReturn(value);

        //when
        Double resultOfOverallCartValue = baseCartService.getOverallCartValue(user.userId());

        //then
        assertEquals(value, resultOfOverallCartValue);
    }

}
