package com.restaurant.contact.controller;

import com.restaurant.contact.controller.dto.ContactRequest;
import com.restaurant.contact.controller.dto.ContactRequestResponse;
import com.restaurant.contact.controller.dto.ContactResponse;
import com.restaurant.contact.service.dto.Contact;
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
