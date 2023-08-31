package com.restaurant.app.contact.controller.validator;

import com.restaurant.app.contact.controller.dto.ContactRequest;

public class ContactValidator {

    public ContactValidator() {
    }

    public boolean isContactRequestNotValid(ContactRequest contactRequest) {
        return !(contactRequest instanceof ContactRequest) ||
                contactRequest.contactMail() == null || contactRequest.contactMail().trim().isEmpty() ||
                contactRequest.contactPhoneNumber() == null || contactRequest.contactPhoneNumber().trim().isEmpty() ||
                contactRequest.openingHoursDays() == null || contactRequest.openingHoursDays().trim().isEmpty() ||
                contactRequest.openingHoursOpenTime() == null || contactRequest.openingHoursOpenTime().trim().isEmpty() ||
                contactRequest.openingHoursCloseTime() == null || contactRequest.openingHoursCloseTime().trim().isEmpty() ||
                contactRequest.addressCity() == null || contactRequest.addressCity().trim().isEmpty() ||
                contactRequest.addressStreet() == null || contactRequest.addressStreet().trim().isEmpty() ||
                contactRequest.addressNumber() == null || contactRequest.addressNumber() <= 0;
    }
}