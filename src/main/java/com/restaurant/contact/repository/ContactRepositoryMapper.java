package com.restaurant.contact.repository;

import com.restaurant.contact.repository.dto.ContactEntity;
import com.restaurant.contact.repository.dto.ContactModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactRepositoryMapper {

    ContactRepositoryMapper INSTANCE = Mappers.getMapper(ContactRepositoryMapper.class);

    ContactModel contactEntityToContactModel(ContactEntity contactEntity);

    ContactEntity contactModelToContactEntity(ContactModel contactModel);
}
