package com.restaurant.contact.controller.dto;

public record ContactRequest(
        String contactMail,
        String contactPhoneNumber,
        String openingHoursDays,
        String openingHoursOpenTime,
        String openingHoursCloseTime,
        String addressCity,
        String addressStreet,
        Integer addressNumber
) {
}
