package myprojectmessenger.dao;

import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.Message;
import myprojectmessenger.entity.User;
import myprojectmessenger.exception.BlockedByUserException;
import myprojectmessenger.exception.NotInChatException;
import myprojectmessenger.model.ChatType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Component
public class MessageDao {
    private final EntityManager entityManager;

    public MessageDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<Message> getMessages(Chat chat, User user, Integer limit) {
        boolean userContainsInChat = (boolean) entityManager.createQuery("select count(cu) > 0 from ChatUser cu where cu.user = :user and cu.chat = :chat")
                .setParameter("user", user)
                .setParameter("chat", chat)
                .getSingleResult();
        if (!userContainsInChat) {
            throw new NotInChatException();
        }
        return entityManager.createQuery("select m from Message m where m.chat =: chat order by m.date")
                .setParameter("chat", chat)
                .setMaxResults(limit)
                .getResultList();
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
