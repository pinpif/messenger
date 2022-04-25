package myprojectmessenger.controllers;

import myprojectmessenger.model.ContactModel;
import myprojectmessenger.service.ContactService;
import myprojectmessenger.service.SessionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/api/contact")
    public void addContact(@RequestHeader(name = SessionService.SESSION_HEADER_NAME) String sessionId,
                             @RequestBody ContactModel contactModel) {
        contactService.addContact(sessionId, contactModel);
    }
}
