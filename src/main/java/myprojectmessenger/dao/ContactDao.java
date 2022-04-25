package myprojectmessenger.dao;

import myprojectmessenger.entity.Contact;
import myprojectmessenger.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
public class ContactDao {
    private final EntityManager entityManager;

    public ContactDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void addContact(User user, User userContact) {
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setFriend(userContact);
        entityManager.persist(contact);
    }
}
