package myprojectmessenger.dao;

import lombok.AllArgsConstructor;
import myprojectmessenger.entity.Session;
import myprojectmessenger.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class AuthorizationDao {
    private final EntityManager entityManager;

    public User findUserByLoginAndPassword(String login, String password) {
        List<User> resultList = entityManager.createQuery("select u from User u where u.account.login = :login and u.account.password = :password")
                .setParameter("login", login)
                .setParameter("password", password)
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
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

    public User findUserBySessionId(String sessionId) {
        return (User) entityManager.createQuery("select s.user from Session s where s.sessionId = :sessionId")
                .setParameter("sessionId", sessionId)
                .getSingleResult();
    }

    public User findUserFromLogin(String login) {
        return (User) entityManager.createQuery("select u from User where u.account.login = :login")
                .setParameter("login",login)
                .getSingleResult();
    }
    public List<User> findUsersByIds(Collection<Long> ids) {
       return entityManager.createQuery("select u from User u where u.id in (:ids)")
                .setParameter("ids",ids).getResultList();
    }
    public void logout(String sessionId){
        entityManager.createQuery("delete  from Session s where s.sessionId=:sessionId")
                .setParameter("sessionId",sessionId)
                .executeUpdate();

    }
}
