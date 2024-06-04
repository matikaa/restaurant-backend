package com.restaurant.services;

import com.restaurant.cart.repository.current.dto.CartModel;
import com.restaurant.cart.service.current.dto.Cart;
import com.restaurant.contact.repository.dto.ContactModel;
import com.restaurant.contact.service.dto.Contact;
import com.restaurant.food.controller.dto.FoodRequest;
import com.restaurant.food.repository.dto.FoodModel;
import com.restaurant.food.service.dto.Food;
import com.restaurant.user.controller.dto.ChangePasswordRequest;
import com.restaurant.user.controller.dto.UpdateUserRequest;
import com.restaurant.user.controller.dto.UserChangePasswordRequest;
import com.restaurant.user.controller.dto.UserRequest;
import com.restaurant.user.repository.dto.UserModel;
import com.restaurant.user.service.dto.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class BaseTestUseCase {

    public User getWithUser() {
        return new User(
                1L,
                "michael@gmail.com",
                "Michael",
                LocalDateTime.now().withNano(0),
                "USER",
                "Wall street 55, 00-355 Florida",
                "0359683552",
                1250D,
                true
        );
    }

    public User getWithoutUser() {
        return new User(
                1L,
                "michael@gmail.com",
                "Michael",
                LocalDateTime.now().withNano(0),
                "USER",
                "Wall street 55, 00-355 Florida",
                "0359683552",
                25D,
                false
        );
    }

    public UserModel getUserModel() {
        return new UserModel(
                1L,
                "michael@gmail.com",
                "Michael",
                "password123",
                LocalDateTime.now().withNano(0),
                "USER",
                "Wall street 55, 00-355 Florida",
                "0359683552",
                1250D,
                true
        );
    }

    public UserModel getUserModelToSave() {
        return new UserModel(
                null,
                "michael@gmail.com",
                "Michael",
                null,
                LocalDateTime.now().withNano(0),
                "USER",
                "Wall street 55, 00-355 Florida",
                "0359683552",
                0D,
                false
        );
    }

    public UserRequest getUserRequest() {
        return new UserRequest(
                "michael@gmail.com",
                "Michael",
                "password123",
                "Wall street 55, 00-355 Florida",
                "0359683552"
        );
    }

    public UpdateUserRequest getUpdateUserRequest() {
        return new UpdateUserRequest(
                "Michael",
                "Wall street 55, 00-355 Florida",
                "0359683552"
        );
    }

    public ChangePasswordRequest getChangePasswordRequest() {
        return new ChangePasswordRequest(
                "password123",
                "StrongPassword@123"
        );
    }

    public UserChangePasswordRequest getUserChangePasswordRequest() {
        return new UserChangePasswordRequest(
                "StrongPassword@123"
        );
    }

    public Authentication getAuthentication() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    public Food getFood() {
        return new Food(
                1L,
                1L,
                1L,
                "Beer",
                50D
        );
    }

    public Food getFoodToUpdate() {
        return new Food(
                2L,
                6L,
                3L,
                "spaghetti bolognese",
                85D
        );
    }

    public FoodModel getFoodModel() {
        return new FoodModel(
                1L,
                1L,
                1L,
                "Beer",
                50D
        );
    }

    public FoodModel getFoodModelToUpdate() {
        return new FoodModel(
                2L,
                6L,
                3L,
                "spaghetti bolognese",
                85D
        );
    }

    public FoodRequest getFoodRequest() {
        return new FoodRequest(
                1L,
                "Beer",
                50D
        );
    }

    public Food getWithoutFood() {
        return new Food(
                1L,
                1L,
                1L,
                "Beer",
                10D
        );
    }

    public CartModel getWithCartModel() {
        return new CartModel(
                1L,
                1L,
                true,
                55D,
                List.of("Beer"),
                List.of(50D)
        );
    }

    public Cart getCartFromCartModel(CartModel cartModel) {
        return new Cart(
                cartModel.cartId(),
                cartModel.userId(),
                cartModel.loyaltyCard(),
                cartModel.cartValue(),
                cartModel.food(),
                cartModel.foodPrice()
        );
    }

    public Contact createContact(Long contactId) {
        return new Contact(
                contactId,
                "contact@mail.com",
                "421932043",
                "monday-saturday",
                "9.00",
                "21.00",
                "Warsaw",
                "Golden Street",
                42
        );
    }

    public ContactModel createContactModel(Long contactId) {
        return new ContactModel(
                contactId,
                "contact@mail.com",
                "421932043",
                "monday-saturday",
                "9.00",
                "21.00",
                "Warsaw",
                "Golden Street",
                42
        );
    }
}
