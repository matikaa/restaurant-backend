package com.restaurant.app.contact.repository;

import com.restaurant.app.contact.repository.dto.ContactModel;

import java.util.Optional;

public interface ContactRepository{

    Optional<ContactModel> findContactByContactId(Long contactId);

    ContactModel saveContact(ContactModel contactModel);

    Optional<ContactModel> update(ContactModel contactModel);

    void delete(Long contactId);

    Boolean existsById(Long contactId);

    Boolean existsAny();
}
