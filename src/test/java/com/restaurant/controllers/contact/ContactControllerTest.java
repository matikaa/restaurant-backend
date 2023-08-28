package com.restaurant.controllers.contact;

import com.restaurant.app.contact.controller.dto.ContactRequestResponse;
import com.restaurant.app.contact.controller.dto.ContactResponse;
import com.restaurant.controllers.TestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

class ContactControllerTest extends TestUseCase {

    @Test
    @DisplayName("Should add contact info and return 200 OK")
    void shouldAddContactAndReturnOk() {
        //given
        var contactRequest = createContactRequest();

        //when
        var contactResponse = client.postForEntity(
                prepareUrl(CONTACT_URL),
                contactRequest,
                ContactRequestResponse.class
        );

        //then
        assertThat(contactResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(contactResponse.getBody(), is(notNullValue()));
        assertThat(contactResponse.getBody().contactMail(), is(equalTo(contactRequest.contactMail())));
        assertThat(contactResponse.getBody().contactPhoneNumber(), is(equalTo(contactRequest.contactPhoneNumber())));
        assertThat(contactResponse.getBody().openingHoursDays(), is(equalTo(contactRequest.openingHoursDays())));
        assertThat(contactResponse.getBody().openingHoursOpenTime(), is(equalTo(contactRequest.openingHoursOpenTime())));
        assertThat(contactResponse.getBody().openingHoursCloseTime(), is(equalTo(contactRequest.openingHoursCloseTime())));
        assertThat(contactResponse.getBody().addressCity(), is(equalTo(contactRequest.addressCity())));
        assertThat(contactResponse.getBody().addressStreet(), is(equalTo(contactRequest.addressStreet())));
        assertThat(contactResponse.getBody().addressNumber(), is(equalTo(contactRequest.addressNumber())));
    }

    @Test
    @DisplayName("Should not add contact and return 400 BAD REQUEST")
    void shouldNotAddContactAndReturnBadRequest() {
        //given
        var contactRequest = createContactRequest();

        //when
        var contactResponse = client.postForEntity(
                prepareUrl(CONTACT_URL),
                contactRequest,
                ContactRequestResponse.class
        );

        //then
        assertThat(contactResponse.getStatusCode(), is(equalTo(CREATED)));
        assertThat(contactResponse.getBody(), is(notNullValue()));
        assertThat(contactResponse.getBody().contactMail(), is(equalTo(contactRequest.contactMail())));
        assertThat(contactResponse.getBody().contactPhoneNumber(), is(equalTo(contactRequest.contactPhoneNumber())));
        assertThat(contactResponse.getBody().openingHoursDays(), is(equalTo(contactRequest.openingHoursDays())));
        assertThat(contactResponse.getBody().openingHoursOpenTime(), is(equalTo(contactRequest.openingHoursOpenTime())));
        assertThat(contactResponse.getBody().openingHoursCloseTime(), is(equalTo(contactRequest.openingHoursCloseTime())));
        assertThat(contactResponse.getBody().addressCity(), is(equalTo(contactRequest.addressCity())));
        assertThat(contactResponse.getBody().addressStreet(), is(equalTo(contactRequest.addressStreet())));
        assertThat(contactResponse.getBody().addressNumber(), is(equalTo(contactRequest.addressNumber())));

        //when
        contactResponse = client.postForEntity(
                prepareUrl(CONTACT_URL),
                contactRequest,
                ContactRequestResponse.class
        );

        //then
        assertThat(contactResponse.getStatusCode(), is(equalTo(BAD_REQUEST)));
    }

    @Test
    @DisplayName("Should get existing contact and return 200 OK")
    void shouldGetContactAndReturnOK() {
        //given
        var savedContact = saveContact();

        //when
        var contactResponse = client.getForEntity(
                prepareGetContactUrl(savedContact.contactId()),
                ContactResponse.class
        );

        //then
        assertThat(contactResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(contactResponse.getBody(), is(notNullValue()));
        assertThat(contactResponse.getBody().contactMail(), is(equalTo(savedContact.contactMail())));
        assertThat(contactResponse.getBody().contactPhoneNumber(), is(equalTo(savedContact.contactPhoneNumber())));
        assertThat(contactResponse.getBody().openingHoursDays(), is(equalTo(savedContact.openingHoursDays())));
        assertThat(contactResponse.getBody().openingHoursOpenTime(), is(equalTo(savedContact.openingHoursOpenTime())));
        assertThat(contactResponse.getBody().openingHoursCloseTime(), is(equalTo(savedContact.openingHoursCloseTime())));
        assertThat(contactResponse.getBody().addressCity(), is(equalTo(savedContact.addressCity())));
        assertThat(contactResponse.getBody().addressStreet(), is(equalTo(savedContact.addressStreet())));
        assertThat(contactResponse.getBody().addressNumber(), is(equalTo(savedContact.addressNumber())));
    }

    @Test
    @DisplayName("Should not get contact and return 404 NOT FOUND")
    void shouldGetContactAndReturnNotFound() {
        //given
        Long notExistingContactId = -3L;

        //when
        var contactResponse = client.getForEntity(
                prepareGetContactUrl(notExistingContactId),
                ContactResponse.class
        );

        //then
        assertThat(contactResponse.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should delete contact and return 200 OK")
    void shouldDeleteContactAndReturnOk() {
        //given
        var savedContact = saveContact();

        //when
        var contactResponse = client.getForEntity(
                prepareGetContactUrl(savedContact.contactId()),
                ContactResponse.class
        );

        //then
        assertThat(contactResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(contactResponse.getBody(), is(notNullValue()));
        assertThat(contactResponse.getBody().contactMail(), is(equalTo(savedContact.contactMail())));
        assertThat(contactResponse.getBody().contactPhoneNumber(), is(equalTo(savedContact.contactPhoneNumber())));
        assertThat(contactResponse.getBody().openingHoursDays(), is(equalTo(savedContact.openingHoursDays())));
        assertThat(contactResponse.getBody().openingHoursOpenTime(), is(equalTo(savedContact.openingHoursOpenTime())));
        assertThat(contactResponse.getBody().openingHoursCloseTime(), is(equalTo(savedContact.openingHoursCloseTime())));
        assertThat(contactResponse.getBody().addressCity(), is(equalTo(savedContact.addressCity())));
        assertThat(contactResponse.getBody().addressStreet(), is(equalTo(savedContact.addressStreet())));
        assertThat(contactResponse.getBody().addressNumber(), is(equalTo(savedContact.addressNumber())));

        //when
        client.delete(prepareGetContactUrl(savedContact.contactId()));

        var contactDeleted = client.getForEntity(
                prepareGetContactUrl(savedContact.contactId()),
                ContactResponse.class
        );

        //then
        assertThat(contactDeleted.getStatusCode(), is(equalTo(NOT_FOUND)));
    }

    @Test
    @DisplayName("Should not delete contact and return 404 NOT FOUND")
    void shouldNotDeleteContact() {
        //given
        var savedContact = saveContact();
        Long notExistingContactId = -1L;

        //when
        var contactResponse = client.getForEntity(
                prepareGetContactUrl(savedContact.contactId()),
                ContactResponse.class
        );

        //then
        assertThat(contactResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(contactResponse.getBody(), is(notNullValue()));
        assertThat(contactResponse.getBody().contactMail(), is(equalTo(savedContact.contactMail())));
        assertThat(contactResponse.getBody().contactPhoneNumber(), is(equalTo(savedContact.contactPhoneNumber())));
        assertThat(contactResponse.getBody().openingHoursDays(), is(equalTo(savedContact.openingHoursDays())));
        assertThat(contactResponse.getBody().openingHoursOpenTime(), is(equalTo(savedContact.openingHoursOpenTime())));
        assertThat(contactResponse.getBody().openingHoursCloseTime(), is(equalTo(savedContact.openingHoursCloseTime())));
        assertThat(contactResponse.getBody().addressCity(), is(equalTo(savedContact.addressCity())));
        assertThat(contactResponse.getBody().addressStreet(), is(equalTo(savedContact.addressStreet())));
        assertThat(contactResponse.getBody().addressNumber(), is(equalTo(savedContact.addressNumber())));

        //when
        client.delete(prepareGetContactUrl(notExistingContactId));

        var contactNotDeleted = client.getForEntity(
                prepareGetContactUrl(savedContact.contactId()),
                ContactResponse.class
        );

        //then
        assertThat(contactNotDeleted.getStatusCode(), is(equalTo(OK)));
        assertThat(contactNotDeleted.getBody(), is(notNullValue()));
        assertThat(contactNotDeleted.getBody().contactMail(), is(equalTo(savedContact.contactMail())));
        assertThat(contactNotDeleted.getBody().contactPhoneNumber(), is(equalTo(savedContact.contactPhoneNumber())));
        assertThat(contactNotDeleted.getBody().openingHoursDays(), is(equalTo(savedContact.openingHoursDays())));
        assertThat(contactNotDeleted.getBody().openingHoursOpenTime(), is(equalTo(savedContact.openingHoursOpenTime())));
        assertThat(contactNotDeleted.getBody().openingHoursCloseTime(), is(equalTo(savedContact.openingHoursCloseTime())));
        assertThat(contactNotDeleted.getBody().addressCity(), is(equalTo(savedContact.addressCity())));
        assertThat(contactNotDeleted.getBody().addressStreet(), is(equalTo(savedContact.addressStreet())));
        assertThat(contactNotDeleted.getBody().addressNumber(), is(equalTo(savedContact.addressNumber())));
    }

    @Test
    @DisplayName("Should update contact and return 200 OK")
    void shouldUpdateContactAndReturnOK() {
        //given
        var savedContact = saveContact();
        var contactRequest = createUpdateContactRequest();

        //when
        var updateResponse = client.exchange(
                prepareGetContactUrl(savedContact.contactId()),
                PUT,
                createBody(contactRequest),
                ContactResponse.class
        );

        //then
        assertThat(updateResponse.getStatusCode(), is(equalTo(OK)));
        assertThat(updateResponse.getBody(), is(notNullValue()));
        assertThat(updateResponse.getBody().contactMail(), is(equalTo(contactRequest.contactMail())));
        assertThat(updateResponse.getBody().contactPhoneNumber(), is(equalTo(contactRequest.contactPhoneNumber())));
        assertThat(updateResponse.getBody().openingHoursDays(), is(equalTo(contactRequest.openingHoursDays())));
        assertThat(updateResponse.getBody().openingHoursOpenTime(), is(equalTo(contactRequest.openingHoursOpenTime())));
        assertThat(updateResponse.getBody().openingHoursCloseTime(), is(equalTo(contactRequest.openingHoursCloseTime())));
        assertThat(updateResponse.getBody().addressCity(), is(equalTo(contactRequest.addressCity())));
        assertThat(updateResponse.getBody().addressStreet(), is(equalTo(contactRequest.addressStreet())));
        assertThat(updateResponse.getBody().addressNumber(), is(equalTo(contactRequest.addressNumber())));
    }

    @Test
    @DisplayName("Should update contact and return 200 NOT FOUND")
    void shouldNotUpdateContactAndReturnNotFound() {
        //given
        var savedContact = saveContact();
        var contactRequest = createUpdateContactRequest();
        Long notExistingContactId = -1L;

        //when
        var updateResponse = client.exchange(
                prepareGetContactUrl(notExistingContactId),
                PUT,
                createBody(contactRequest),
                ContactResponse.class
        );

        //then
        assertThat(updateResponse.getStatusCode(), is(equalTo(NOT_FOUND)));

        //when
        var contactNotDeleted = client.getForEntity(
                prepareGetContactUrl(savedContact.contactId()),
                ContactResponse.class
        );

        //then
        assertThat(contactNotDeleted.getStatusCode(), is(equalTo(OK)));
        assertThat(contactNotDeleted.getBody(), is(notNullValue()));
        assertThat(contactNotDeleted.getBody().contactMail(), is(equalTo(savedContact.contactMail())));
        assertThat(contactNotDeleted.getBody().contactPhoneNumber(), is(equalTo(savedContact.contactPhoneNumber())));
        assertThat(contactNotDeleted.getBody().openingHoursDays(), is(equalTo(savedContact.openingHoursDays())));
        assertThat(contactNotDeleted.getBody().openingHoursOpenTime(), is(equalTo(savedContact.openingHoursOpenTime())));
        assertThat(contactNotDeleted.getBody().openingHoursCloseTime(), is(equalTo(savedContact.openingHoursCloseTime())));
        assertThat(contactNotDeleted.getBody().addressCity(), is(equalTo(savedContact.addressCity())));
        assertThat(contactNotDeleted.getBody().addressStreet(), is(equalTo(savedContact.addressStreet())));
        assertThat(contactNotDeleted.getBody().addressNumber(), is(equalTo(savedContact.addressNumber())));
    }
}