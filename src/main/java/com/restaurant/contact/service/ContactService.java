package com.restaurant.contact.service;

import com.restaurant.contact.service.dto.Contact;

import java.util.Optional;

public interface ContactService {

    Optional<Contact> getContact(Long contactId);

    Contact addContact(Contact contact);

    Optional<Contact> update(Contact contact);

    void delete(Long contactId);

    boolean existsById(Long contactId);

    boolean existsAny();
}
