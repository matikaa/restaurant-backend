package com.restaurant.app.contact.repository;

import com.restaurant.app.contact.repository.dto.ContactModel;

import java.util.Optional;

public class JpaWrappedContactRepository implements ContactRepository {

    private static final ContactRepositoryMapper contactRepositoryMapper = ContactRepositoryMapper.INSTANCE;

    private final ContactJpaRepository contactJpaRepository;

    public JpaWrappedContactRepository(ContactJpaRepository contactJpaRepository) {
        this.contactJpaRepository = contactJpaRepository;
    }

    @Override
    public Optional<ContactModel> findContactByContactId(Long contactId) {
        return contactJpaRepository.findById(contactId)
                .map(contactRepositoryMapper::contactEntityToContactModel);
    }

    @Override
    public ContactModel saveContact(ContactModel contactModel) {
        return contactRepositoryMapper.contactEntityToContactModel(contactJpaRepository
                .save(contactRepositoryMapper.contactModelToContactEntity(contactModel)));
    }

    @Override
    public Optional<ContactModel> update(ContactModel contact) {
        return contactJpaRepository.findById(contact.contactId())
                .map(contactToUpdate -> contactRepositoryMapper.contactModelToContactEntity(contact))
                .map(contactJpaRepository::save)
                .map(contactRepositoryMapper::contactEntityToContactModel);
    }

    @Override
    public void delete(Long contactId) {
        contactJpaRepository.deleteById(contactId);
    }

    @Override
    public Boolean existsById(Long contactId) {
        return contactJpaRepository.existsById(contactId);
    }

    @Override
    public Boolean existsAny() {
        return !contactJpaRepository.findAll().isEmpty();
    }
}
