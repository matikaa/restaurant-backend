package com.restaurant.cart.repository.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.common.ConstantValues;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Collections.emptyList;

@Converter
public class FoodPriceConverter implements AttributeConverter<List<Double>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodPriceConverter.class);

    @Override
    public String convertToDatabaseColumn(List<Double> listOfFoodPrice) {
        if (listOfFoodPrice == null) {
            LOGGER.warn(ConstantValues.INVALID_CONVERT);
            return ConstantValues.EMPTY_STRING;
        }

        if (listOfFoodPrice.isEmpty()) {
            return ConstantValues.EMPTY_STRING;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(listOfFoodPrice);
        } catch (JsonProcessingException ex) {
            LOGGER.warn(ConstantValues.INVALID_CONVERT);
            return ConstantValues.EMPTY_STRING;
        }
    }

    @Override
    public List<Double> convertToEntityAttribute(String foodPrice) {
        if (foodPrice == null) {
            LOGGER.warn(ConstantValues.INVALID_CONVERT);
            return emptyList();
        }

        if (foodPrice.isEmpty()) {
            return emptyList();
        }

        try {
            return OBJECT_MAPPER.readValue(foodPrice, new TypeReference<>() {
            });
        } catch (JsonProcessingException ex) {
            LOGGER.warn(ConstantValues.INVALID_CONVERT);
            return emptyList();
        }
    }
}
