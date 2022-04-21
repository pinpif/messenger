package myprojectmessenger.dao;

import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
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
        entityManager.persist(chat);
        return chat;
    }

    public Chat findById(long id) {
        return entityManager.find(Chat.class, id);
    }
}
