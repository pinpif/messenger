package myprojectmessenger.dao;

import myprojectmessenger.entity.BlockList;
import myprojectmessenger.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Component
public class UserDao {
    private final EntityManager entityManager;

    public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<User> findUsers(String nameOrLoginUser, int limit) {
        return entityManager.createQuery("select u from User u left join u.account a " +
                        "where UPPER(u.name) LIKE UPPER(:nameOrLoginUser) or UPPER(a.login) LIKE UPPER(:nameOrLoginUser)")
                .setParameter("nameOrLoginUser", '%' + nameOrLoginUser + '%')
                .setMaxResults(limit)
                .getResultList();
    }

    @Transactional
    public void blockUser(User user, User userBlock) {
        BlockList blockList = new BlockList();
        blockList.setUser(user);
        blockList.setBlockedUser(userBlock);
        entityManager.persist(blockList);
    }

    @Transactional
    public void unblockUser(User user, User userBlock) {
        entityManager.createQuery("delete from BlockList b where b.user = :user and b.blockedUser = :blockedUser")
                .setParameter("blockedUser", userBlock)
                .setParameter("user", user)
                .executeUpdate();
    }

    public User findUserBySessionId(String sessionId) {
        return (User) entityManager.createQuery("select s.user from Session s where s.sessionId = :sessionId")
                .setParameter("sessionId", sessionId)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<User> findUsersByIds(Collection<Long> ids) {
        return entityManager.createQuery("select u from User u where u.id in (:ids)")
                .setParameter("ids", ids)
                .getResultList();
    }

    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }
}
