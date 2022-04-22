package myprojectmessenger.dao;

import myprojectmessenger.entity.User;
import myprojectmessenger.model.UserSearch;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class SearchDao {
    private final EntityManager entityManager;

    public SearchDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> searchUser(String nameOrLoginUser) {
        return entityManager.createQuery("select u from User u left join u.account a " +
                        "where UPPER(u.name) LIKE UPPER(:nameOrLoginUser) or UPPER(a.login) LIKE UPPER(:nameOrLoginUser)")
                .setParameter("nameOrLoginUser", '%' + nameOrLoginUser + '%')
                .setMaxResults(20).getResultList();
    }
}
