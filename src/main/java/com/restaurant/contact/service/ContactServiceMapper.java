package com.restaurant.contact.service;

import com.restaurant.contact.repository.dto.ContactModel;
import com.restaurant.contact.service.dto.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactServiceMapper {

    ContactServiceMapper INSTANCE = Mappers.getMapper(ContactServiceMapper.class);

    Contact contactModelToContact(ContactModel contactModel);

    ContactModel contactToContactModel(Contact contact);
}
