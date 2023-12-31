package com.restaurant.contact.repository;

import com.restaurant.contact.repository.dto.ContactModel;

import java.util.Optional;

public interface ContactRepository {

    Optional<ContactModel> findContactByContactId(Long contactId);

    ContactModel saveContact(ContactModel contactModel);

    Optional<ContactModel> update(ContactModel contactModel);

    void delete(Long contactId);

    boolean existsById(Long contactId);

    boolean existsAny();
}
