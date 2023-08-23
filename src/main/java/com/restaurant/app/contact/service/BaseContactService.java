package com.restaurant.app.contact.service;

import com.restaurant.app.contact.repository.ContactRepository;
import com.restaurant.app.contact.service.dto.Contact;

import java.util.Optional;

public class BaseContactService implements ContactService{

    private static final ContactServiceMapper contactServiceMapper = ContactServiceMapper.INSTANCE;

    private final ContactRepository contactRepository;

    public BaseContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Optional<Contact> getContact(Long contactId) {
        return contactRepository.findContactByContactId(contactId)
                .map(contactServiceMapper::contactModelToContact);
    }

    @Override
    public Contact addContact(Contact contact) {
        return contactServiceMapper.contactModelToContact(
                contactRepository.saveContact(contactServiceMapper.contactToContactModel(contact)));
    }

    @Override
    public Optional<Contact> update(Contact contact){
        return contactRepository.update(contactServiceMapper.contactToContactModel(contact))
                .map(contactServiceMapper::contactModelToContact);
    }

    @Override
    public void delete(Long contactId){
        contactRepository.delete(contactId);
    }

    @Override
    public Boolean existsById(Long contactId){
        return contactRepository.existsById(contactId);
    }

    @Override
    public Boolean existsAny(){
        return contactRepository.existsAny();
    }
}
