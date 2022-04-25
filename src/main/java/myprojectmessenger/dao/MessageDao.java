package myprojectmessenger.dao;

import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.Message;
import myprojectmessenger.entity.MessageState;
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
    public void addMessage(Chat chat, String text, Date date, User author) {
        if (chat.getType() == ChatType.PERSONAL) {
            Boolean isBlockedByUser = (Boolean) entityManager
                    .createQuery("select count(c) > 0 from Chat c " +
                            "join c.users u " +
                            "join u.blockLists b " +
                            "where c = :chat and u != :author and b.blockedUser = :author")
                    .setParameter("author", author)
                    .setParameter("chat", chat)
                    .getSingleResult();
            if (isBlockedByUser) {
                throw new BlockedByUserException();
            }
        }
        Message message = createMessage(chat, text, date, author);
        entityManager.persist(message);

        saveMessageStates(chat, author, message);
    }

    private void saveMessageStates(Chat chat, User user, Message message) {
        for (User chatUser : chat.getUsers()) {
            MessageState messageState = createMessageState(user, message, chatUser);
            entityManager.persist(messageState);
        }
    }

    private MessageState createMessageState(User author, Message message, User user) {
        MessageState messageState = new MessageState();
        messageState.setMessage(message);
        if (user.equals(author)) {
            messageState.setState(true);
        }
        messageState.setUser(user);
        return messageState;
    }

    private Message createMessage(Chat chat, String text, Date date, User user) {
        Message message = new Message();
        message.setDate(date);
        message.setChat(chat);
        message.setAuthor(user);
        message.setMessage(text);
        return message;
    }
}
