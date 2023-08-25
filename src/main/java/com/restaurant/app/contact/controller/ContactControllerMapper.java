package com.restaurant.app.contact.controller;

import com.restaurant.app.contact.controller.dto.ContactRequest;
import com.restaurant.app.contact.controller.dto.ContactRequestResponse;
import com.restaurant.app.contact.controller.dto.ContactResponse;
import com.restaurant.app.contact.service.dto.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactControllerMapper {

    ContactControllerMapper INSTANCE = Mappers.getMapper(ContactControllerMapper.class);

    ContactResponse contactToContactResponse(Contact contact);

    Contact contactRequestToContact(ContactRequest contactRequest);

    Contact contactRequestToContact(ContactRequest contactRequest, Long contactId);

    ContactRequestResponse contactToContactRequestResponse(Contact contact);
}
