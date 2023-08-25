package com.restaurant.app.contact.service;

import com.restaurant.app.contact.service.dto.Contact;

import java.util.Optional;

public interface ContactService {

    Optional<Contact> getContact(Long contactId);

    Contact addContact(Contact contact);

    Optional<Contact> update(Contact contact);

    void delete(Long contactId);

    Boolean existsById(Long contactId);

    Boolean existsAny();
}
