package com.restaurant.app.contact.service;

import com.restaurant.app.contact.repository.dto.ContactModel;
import com.restaurant.app.contact.service.dto.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactServiceMapper {

    ContactServiceMapper INSTANCE = Mappers.getMapper(ContactServiceMapper.class);

    Contact contactModelToContact(ContactModel contactModel);

    ContactModel contactToContactModel(Contact contact);
}
