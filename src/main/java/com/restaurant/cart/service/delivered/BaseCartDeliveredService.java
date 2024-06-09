package com.restaurant.cart.service.delivered;

import com.restaurant.cart.controller.dto.OrderDate;
import com.restaurant.cart.repository.current.dto.CartModel;
import com.restaurant.cart.repository.delivered.CartDeliveredRepository;
import com.restaurant.cart.repository.delivered.dto.CartDeliveredModel;
import com.restaurant.cart.service.current.dto.SoldFoodSummary;
import com.restaurant.cart.service.delivered.dto.CartDelivered;
import com.restaurant.category.service.CategoryService;
import com.restaurant.category.service.dto.Category;
import com.restaurant.food.service.FoodService;
import com.restaurant.food.service.dto.Food;

import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;

public class BaseCartDeliveredService implements CartDeliveredService {

    private static final CartDeliveredServiceMapper cartDeliveredServiceMapper = CartDeliveredServiceMapper.INSTANCE;

    private final CartDeliveredRepository cartDeliveredRepository;

    private final FoodService foodService;

    private final CategoryService categoryService;

    public BaseCartDeliveredService(CartDeliveredRepository cartDeliveredRepository, FoodService foodService, CategoryService categoryService) {
        this.cartDeliveredRepository = cartDeliveredRepository;
        this.foodService = foodService;
        this.categoryService = categoryService;
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
    public Double sumAllSoldFood(OrderDate orderDate) {
        ZonedDateTime beforeDate = getDateBefore(orderDate);

        return cartDeliveredRepository.findAllOrders()
                .stream()
                .filter(delivered -> delivered.orderDate().isAfter(beforeDate))
                .mapToDouble(food -> food.cartValue())
                .sum();
    }

    @Override
    public List<SoldFoodSummary> findAllUsersOrders(OrderDate orderDate) {
        ZonedDateTime beforeDate = getDateBefore(orderDate);
        var allOrders = cartDeliveredRepository.findAllOrders()
                .stream()
                .filter(delivered -> delivered.orderDate().isAfter(beforeDate))
                .toList();

        var allCategories = categoryService.getAll();
        var categoryMap = allCategories.stream()
                .collect(Collectors.toMap(Category::categoryId, category -> category));

        var foodSummaryMap = allOrders.stream()
                .flatMap(order -> {
                    List<String> foodList = order.food();
                    List<Double> priceList = order.foodPrice();
                    return IntStream.range(0, foodList.size())
                            .mapToObj(i -> new AbstractMap.SimpleEntry<>(foodList.get(i), priceList.get(i)));
                })
                .collect(Collectors.groupingBy(
                        AbstractMap.SimpleEntry::getKey,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                entries -> {
                                    String foodName = entries.get(0).getKey();
                                    Double price = entries.get(0).getValue();
                                    Integer quantitySold = entries.size();
                                    Double totalValue = price * quantitySold;
                                    return new SoldFoodSummary(foodName, price, quantitySold, totalValue,
                                            null, null, null);
                                }
                        )
                ));

        foodSummaryMap.replaceAll((foodName, summary) -> {
            Optional<Food> foodItem = foodService.getFoodByNameAndPrice(foodName, summary.price());
            if (foodItem.isPresent()) {
                Food foodDetails = foodItem.get();
                Category categoryDetails = categoryMap.get(foodDetails.categoryId());
                return new SoldFoodSummary(
                        foodName,
                        summary.price(),
                        summary.quantitySold(),
                        summary.totalValue(),
                        foodDetails.categoryId(),
                        categoryDetails != null ? categoryDetails.categoryName() : null,
                        foodDetails.positionId()
                );
            } else {
                return summary;
            }
        });

        if (foodSummaryMap.isEmpty()) {
            return emptyList();
        }

        return foodSummaryMap.values().stream()
                .sorted(Comparator.comparing(SoldFoodSummary::categoryId)
                        .thenComparing(SoldFoodSummary::positionId))
                .toList();
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return cartDeliveredRepository.existsByUserId(userId);
    }

    ZonedDateTime getDateBefore(OrderDate orderDate) {
        if(orderDate.orderDate().equals("month")) {
            return ZonedDateTime.now().minusMonths(1);
        } else if(orderDate.orderDate().equals("half_year")) {
            return ZonedDateTime.now().minusMonths(6);
        }
        return ZonedDateTime.now().minusMonths(12);
    }
}
