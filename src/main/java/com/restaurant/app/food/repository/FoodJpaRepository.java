package com.restaurant.app.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodJpaRepository extends JpaRepository<FoodEntity, Long> {

    List<FoodEntity> getFoodByCategoryId(Long categoryId);

    Optional<FoodEntity> getFoodByCategoryIdAndFoodId(Long categoryId, Long foodId);
}