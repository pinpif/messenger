package myprojectmessenger.dao;

import myprojectmessenger.entity.BlockList;
import myprojectmessenger.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
public class BlockDao {
    private final EntityManager entityManager;

    public BlockDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void addBlock(User user, User userBlock) {
        BlockList blockList = new BlockList();
        blockList.setUser(user);
        blockList.setBlockedUser(userBlock);
        entityManager.persist(blockList);
    }

    @Transactional
    public void unblockUser(User user, User userBlock) {
        entityManager.createQuery("delete from BlockList b where b.user = :user and b.blockedUser = :blockedUser")
                .setParameter("blockedUser",userBlock)
                .setParameter("user",user)
                .executeUpdate();
    }
}
