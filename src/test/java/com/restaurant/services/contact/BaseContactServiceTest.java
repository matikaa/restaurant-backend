package com.restaurant.services.contact;

import com.restaurant.contact.repository.ContactRepository;
import com.restaurant.contact.repository.dto.ContactModel;
import com.restaurant.contact.service.BaseContactService;
import com.restaurant.contact.service.dto.Contact;
import com.restaurant.services.BaseTestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseContactServiceTest extends BaseTestUseCase {

    @InjectMocks
    private BaseContactService baseContactService;

    @Mock
    private ContactRepository contactRepository;

    @Test
    @DisplayName("Should get contact by contactId")
    void shouldGetContactByContactId() {
        //given
        Long contactId = 1L;
        Optional<Contact> contact = Optional.of(createContact(contactId));
        Optional<ContactModel> contactModel = Optional.of(createContactModel(contactId));

        when(contactRepository.findContactByContactId(contactId)).thenReturn(contactModel);

        //when
        Optional<Contact> contactResult = baseContactService.getContact(contactId);

        //then
        assertEquals(contact, contactResult);
    }

    @Test
    @DisplayName("Should add and return contact")
    void shouldAddContact() {
        //given
        Long contactId = 5L;
        Contact contact = createContact(contactId);
        ContactModel contactModel = createContactModel(contactId);

        when(contactRepository.saveContact(contactModel)).thenReturn(contactModel);

        //when
        Contact savedContact = baseContactService.addContact(contact);

        //then
        assertEquals(contact, savedContact);
    }

    @Test
    @DisplayName("Should update and return contact")
    void shouldUpdateContact() {
        //given
        Long contactId = 3L;
        Contact contactToUpdate = createContact(contactId);
        ContactModel contactModel = createContactModel(contactId);
        Optional<Contact> contact = Optional.of(createContact(contactId));

        when(contactRepository.update(contactModel)).thenReturn(Optional.of(contactModel));

        //when
        Optional<Contact> updatedContact = baseContactService.update(contactToUpdate);

        //assert
        assertEquals(contact, updatedContact);
    }

    @Test
    @DisplayName("Should return true when contact exists by Id")
    void shouldReturnTrueWhenExistsById() {
        //given
        Long contactId = 15L;
        boolean contactExist = true;

        when(contactRepository.existsById(contactId)).thenReturn(contactExist);

        //when
        boolean existsResult = baseContactService.existsById(contactId);

        //then
        assertEquals(contactExist, existsResult);
    }

    @Test
    @DisplayName("Should return that any contact exists")
    void shouldReturnThatAnyContactExists() {
        //given
        boolean anyContactExists = true;

        when(contactRepository.existsAny()).thenReturn(anyContactExists);

        //when
        boolean anyContactExistsResult = baseContactService.existsAny();

        //then
        assertEquals(anyContactExists, anyContactExistsResult);
    }
}
