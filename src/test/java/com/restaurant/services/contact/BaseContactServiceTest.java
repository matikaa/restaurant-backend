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

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
