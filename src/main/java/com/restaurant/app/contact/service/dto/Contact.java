package com.restaurant.app.contact.service.dto;

public record Contact(
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
