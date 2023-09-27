package com.restaurant.cart.repository.delivered;

import com.restaurant.cart.repository.converter.FoodNameConverter;
import com.restaurant.cart.repository.converter.FoodPriceConverter;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class CartDeliveredEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long cartId;

    private Long userId;

    private Boolean loyaltyCard;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double cartValue;

    @Column
    @Convert(converter = FoodNameConverter.class)
    private List<String> food = new ArrayList<>();

    @Column
    @Convert(converter = FoodPriceConverter.class)
    private List<Double> foodPrice = new ArrayList<>();

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getLoyaltyCard() {
        return loyaltyCard;
    }

    public void setLoyaltyCard(Boolean loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }

    public Double getCartValue() {
        return cartValue;
    }

    public void setCartValue(Double cartValue) {
        this.cartValue = cartValue;
    }

    public List<String> getFood() {
        return food;
    }

    public void setFood(List<String> food) {
        this.food = food;
    }

    public List<Double> getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(List<Double> foodPrice) {
        this.foodPrice = foodPrice;
    }
}
