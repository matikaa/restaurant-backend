package com.restaurant.app.contact.controller;

import com.restaurant.app.contact.controller.dto.ContactRequest;
import com.restaurant.app.contact.controller.dto.ContactRequestResponse;
import com.restaurant.app.contact.controller.dto.ContactResponse;
import com.restaurant.app.contact.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private static final ContactControllerMapper contactControllerMapper = ContactControllerMapper.INSTANCE;
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<ContactResponse> getContact(@PathVariable Long contactId) {
        return contactService.getContact(contactId)
                .map(contact -> ResponseEntity.ok().body(contactControllerMapper.contactToContactResponse(contact)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ContactRequestResponse> addContact(@RequestBody ContactRequest contactRequest) {
        if(contactService.existsAny()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(CREATED).body(
                contactControllerMapper.contactToContactRequestResponse(
                        contactService.addContact(contactControllerMapper.contactRequestToContact(contactRequest))));
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long contactId,
                                                         @RequestBody ContactRequest contactRequest) {
        return contactService.update(contactControllerMapper.contactRequestToContact(contactRequest, contactId))
                .map(contact -> ResponseEntity.ok().body(contactControllerMapper.contactToContactResponse(contact)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId){
        if(!contactService.existsById(contactId)) {
            return ResponseEntity.notFound().build();
        }

        contactService.delete(contactId);
        return ResponseEntity.ok().build();
    }
}
