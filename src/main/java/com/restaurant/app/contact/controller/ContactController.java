package com.restaurant.app.contact.controller;

import com.restaurant.app.contact.controller.dto.ContactRequest;
import com.restaurant.app.contact.controller.dto.ContactRequestResponse;
import com.restaurant.app.contact.controller.dto.ContactResponse;
import com.restaurant.app.contact.controller.validator.ContactValidator;
import com.restaurant.app.contact.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.restaurant.app.response.ConstantValues.*;
import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/contact")
public class ContactController {

    private static final ContactControllerMapper contactControllerMapper = ContactControllerMapper.INSTANCE;

    private final ContactValidator contactValidator;

    private final ContactService contactService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    public ContactController(ContactService contactService) {
        this.contactValidator = new ContactValidator();
        this.contactService = contactService;
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<ContactResponse> getContact(@PathVariable Long contactId) {
        if (!contactService.existsById(contactId)) {
            LOGGER.warn(CONTACT_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return contactService.getContact(contactId)
                .map(contact -> ResponseEntity.ok().body(contactControllerMapper.contactToContactResponse(contact)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ContactRequestResponse> addContact(@RequestBody ContactRequest contactRequest) {
        if (contactValidator.isContactRequestNotValid(contactRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (contactService.existsAny()) {
            LOGGER.warn(CONTACT_EXISTS);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(CREATED).body(
                contactControllerMapper.contactToContactRequestResponse(
                        contactService.addContact(contactControllerMapper.contactRequestToContact(contactRequest))));
    }

    @PutMapping("/{contactId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long contactId,
                                                         @RequestBody ContactRequest contactRequest) {
        if (contactValidator.isContactRequestNotValid(contactRequest)) {
            LOGGER.warn(INVALID_REQUEST_BODY);
            return ResponseEntity.badRequest().build();
        }

        if (!contactService.existsById(contactId)) {
            LOGGER.warn(CONTACT_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        return contactService.update(contactControllerMapper.contactRequestToContact(contactRequest, contactId))
                .map(contact -> ResponseEntity.ok().body(contactControllerMapper.contactToContactResponse(contact)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{contactId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        if (!contactService.existsById(contactId)) {
            LOGGER.warn(CONTACT_NOT_EXISTS);
            return ResponseEntity.notFound().build();
        }

        contactService.delete(contactId);
        return ResponseEntity.ok().build();
    }
}
