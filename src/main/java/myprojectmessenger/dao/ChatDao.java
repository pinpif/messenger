package myprojectmessenger.dao;

import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.User;
import myprojectmessenger.exception.ChatExists;
import myprojectmessenger.model.ChatType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@Component
public class ChatDao {
    private final EntityManager entityManager;

    public ChatDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public Set<Chat> findRecent10ChatsForUser(User user) {
        return new HashSet<>(entityManager.createQuery("select c from Chat c left join c.users u left join c.messageList m where u.id = :userId order by m.date")
                .setParameter("userId", user.getId())
                .setMaxResults(10)
                .getResultList());
    }

    @Transactional
    public Chat addChat(Chat chat) {
        if (chat.getType().equals(ChatType.PERSONAL)) {
            if (chat.getUsers().size() != 2) {
                throw  new ChatExists(); // todo: add specific exception
            }

            User otherUser = chat.getUsers().stream().filter(user -> !user.equals(chat.getAuthor())).findFirst().orElse(null);
            boolean chatExists = (boolean) entityManager
                    .createQuery("select count(c) > 0 from Chat c " +
                            "left join c.users cu " +
                            "where c.type = 'PERSONAL' and ((c.author = :user and cu = :otherUser) or (c.author = :otherUser and cu = :user))")
                    .setParameter("user", chat.getAuthor())
                    .setParameter("otherUser", otherUser)
                    .getSingleResult();
            if (chatExists) {
                throw new ChatExists();
            }
        }
        entityManager.persist(chat);
        return chat;
    }

    public Chat findById(long id) {
        return entityManager.find(Chat.class, id);
    }
}
