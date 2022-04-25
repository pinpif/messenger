package myprojectmessenger.service;

import myprojectmessenger.dao.ContactDao;
import myprojectmessenger.dao.UserDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.ContactModel;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    private final ContactDao contactDao;
    private final UserDao userDao;

    public ContactService(ContactDao contactDao, UserDao userDao) {
        this.contactDao = contactDao;
        this.userDao = userDao;
    }

    public void addedFriends(String sessionId,
                             ContactModel contactModel) {
        User user = userDao.findUserBySessionId(sessionId);
        User contact = userDao.findUserById(contactModel.getContactId());
        contactDao.addContact(user, contact);
    }
}
