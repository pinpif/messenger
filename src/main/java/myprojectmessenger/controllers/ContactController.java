package myprojectmessenger.controllers;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.dao.ContactDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.ContactModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class ContactController {
    private final AuthorizationDao authorizationDao;
    private final ContactDao contactDao;


    public ContactController(AuthorizationDao authorizationDao, ContactDao contactDao) {
        this.authorizationDao = authorizationDao;
        this.contactDao = contactDao;
    }

    @PostMapping("/api/contact")
    public void addedFriends(@RequestHeader(name = "SessionId") String sessionId,
                             @RequestBody ContactModel contactModel) {
        User user = authorizationDao.findUserBySessionId(sessionId);
        User contact = authorizationDao.findUsersByIds(Collections.singletonList(contactModel.getContactId())).get(0);
        contactDao.addContact(user, contact);
    }
}
