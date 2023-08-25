package com.restaurant.app.contact.repository;

import com.restaurant.app.contact.repository.dto.ContactEntity;
import com.restaurant.app.contact.repository.dto.ContactModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactRepositoryMapper {

    ContactRepositoryMapper INSTANCE = Mappers.getMapper(ContactRepositoryMapper.class);

    ContactModel contactEntityToContactModel(ContactEntity contactEntity);

    ContactEntity contactModelToContactEntity(ContactModel contactModel);
}
