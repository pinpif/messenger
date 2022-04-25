package myprojectmessenger.controllers;

import myprojectmessenger.dao.ContactDao;
import myprojectmessenger.dao.UserDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.ContactModel;
import myprojectmessenger.service.SessionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {
    private final ContactDao contactDao;
    private final UserDao userDao;

    public ContactController(ContactDao contactDao, UserDao userDao) {
        this.contactDao = contactDao;
        this.userDao = userDao;
    }

    @PostMapping("/api/contact")
    public void addedFriends(@RequestHeader(name = SessionService.SESSION_HEADER_NAME) String sessionId,
                             @RequestBody ContactModel contactModel) {
        User user = userDao.findUserBySessionId(sessionId);
        User contact = userDao.findUserById(contactModel.getContactId());
        contactDao.addContact(user, contact);
    }
}
