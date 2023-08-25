package com.restaurant.app.contact.controller.dto;

public record ContactRequestResponse(
        Long contactId,
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
