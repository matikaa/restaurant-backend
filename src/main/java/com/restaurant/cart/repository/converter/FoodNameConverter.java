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
public class FoodNameConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodNameConverter.class);

    @Override
    public String convertToDatabaseColumn(List<String> listOfFood) {
        if (listOfFood == null) {
            LOGGER.warn(ConstantValues.INVALID_CONVERT);
            return ConstantValues.EMPTY_STRING;
        }

        if (listOfFood.isEmpty()) {
            return ConstantValues.EMPTY_STRING;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(listOfFood);
        } catch (JsonProcessingException ex) {
            LOGGER.warn(ConstantValues.INVALID_CONVERT);
            return ConstantValues.EMPTY_STRING;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String food) {
        if (food == null) {
            LOGGER.warn(ConstantValues.INVALID_CONVERT);
            return emptyList();
        }

        if (food.isEmpty()) {
            return emptyList();
        }

        try {
            return OBJECT_MAPPER.readValue(food, new TypeReference<>() {
            });
        } catch (JsonProcessingException ex) {
            LOGGER.warn(ConstantValues.INVALID_CONVERT);
            return emptyList();
        }
    }
}
