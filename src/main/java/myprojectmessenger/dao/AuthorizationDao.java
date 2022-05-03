package myprojectmessenger.dao;

import lombok.AllArgsConstructor;
import myprojectmessenger.entity.Account;
import myprojectmessenger.entity.Session;
import myprojectmessenger.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class AuthorizationDao {
    private final EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public User findUserByLoginAndPassword(String login, String password) {
        List<User> resultList = entityManager.createQuery("select u from User u where u.account.login = :login and u.account.password = :password")
                .setParameter("login", login)
                .setParameter("password", password)
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }
    public Account findUserByMail(String mail) {
       return (Account) entityManager.createQuery("select a.id from Account a where a.mail =: mail")
                .setParameter("mail",mail)
               .getSingleResult();
    }
    @Transactional
    public void addSession(User user, UUID uuid) {
        Session session = new Session();
        session.setAddedDate(new Date());
        session.setSessionId(uuid.toString());
        session.setUser(user);
        entityManager.persist(session);
    }

    public boolean sessionExist(String sessionId) {
        return !entityManager.createQuery("select s from Session s where s.sessionId = :sessionId")
                .setParameter("sessionId", sessionId)
                .getResultList().isEmpty();
    }

    public void logout(String sessionId) {
        entityManager.createQuery("delete  from Session s where s.sessionId=:sessionId")
                .setParameter("sessionId", sessionId)
                .executeUpdate();

    }
}
