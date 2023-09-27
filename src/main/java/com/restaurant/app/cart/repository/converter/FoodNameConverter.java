package com.restaurant.app.cart.repository.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.restaurant.app.common.ConstantValues.EMPTY_STRING;
import static com.restaurant.app.common.ConstantValues.INVALID_CONVERT;
import static java.util.Collections.emptyList;

@Converter
public class FoodNameConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodNameConverter.class);

    @Override
    public String convertToDatabaseColumn(List<String> listOfFood) {
        if (listOfFood == null) {
            LOGGER.warn(INVALID_CONVERT);
            return EMPTY_STRING;
        }

        if (listOfFood.isEmpty()) {
            return EMPTY_STRING;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(listOfFood);
        } catch (JsonProcessingException ex) {
            LOGGER.warn(INVALID_CONVERT);
            return EMPTY_STRING;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String food) {
        if (food == null) {
            LOGGER.warn(INVALID_CONVERT);
            return emptyList();
        }

        if (food.isEmpty()) {
            return emptyList();
        }

        try {
            return OBJECT_MAPPER.readValue(food, new TypeReference<>() {
            });
        } catch (JsonProcessingException ex) {
            LOGGER.warn(INVALID_CONVERT);
            return emptyList();
        }
    }
}
