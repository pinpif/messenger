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
    private final ContactService contactServise;

    public ContactController(ContactService contactServise) {
        this.contactServise = contactServise;

    }

    @PostMapping("/api/contact")
    public void addedFriends(@RequestHeader(name = SessionService.SESSION_HEADER_NAME) String sessionId,
                             @RequestBody ContactModel contactModel) {
        contactServise.addedFriends(sessionId, contactModel);
    }
}
