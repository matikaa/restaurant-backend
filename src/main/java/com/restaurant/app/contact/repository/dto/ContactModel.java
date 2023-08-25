package com.restaurant.app.contact.repository.dto;

public record ContactModel(
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
