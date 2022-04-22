package myprojectmessenger.dao;

import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.Message;
import myprojectmessenger.entity.User;
import myprojectmessenger.exception.BlockedByUserException;
import myprojectmessenger.model.ChatType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

@Component
public class MessageDao {
    private final EntityManager entityManager;

    public MessageDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void addMessage(Chat chat, String text, Date date, User user) {
        if (chat.getType() == ChatType.PERSONAL) {
            Boolean isBlockedByUser = (Boolean) entityManager.createQuery("select count(c) > 0 from Chat c join c.users u join u.blockLists b where u != :user and b.blockedUser = :user")
                    .setParameter("user", user)
                    .getSingleResult();
            if (isBlockedByUser) {
                throw new BlockedByUserException();
            }
        }
        Message message = new Message();
        message.setDate(date);
        message.setChat(chat);
        message.setAuthor(user);
        message.setMessage(text);
        entityManager.persist(message);
    }

    public void addFile(Chat chat, String fileName, byte[] bytes) {

    }

    public void showMessage() {

    }
}
