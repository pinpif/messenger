package myprojectmessenger.dao;

import myprojectmessenger.entity.Account;
import myprojectmessenger.entity.RecoveryPassword;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

@Component
public class MailDao {
    private final EntityManager entityManager;

    public MailDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public RecoveryPassword findRecoveryPasswordByCode(String code) {
        return (RecoveryPassword) entityManager.createQuery("select r from RecoveryPassword where r.code = :code")
                .setParameter("code", code)
                .getResultList().get(0);
    }

    @Transactional
    public void changePassword(Account account, String newPassword) {
        account.setPassword(newPassword);
        entityManager.merge(account);
    }


    @Transactional
    public void saveSession(Account account, String uuid) {
        RecoveryPassword recoveryPassword = new RecoveryPassword();
        recoveryPassword.setAccount(account);
        recoveryPassword.setCode(uuid);
        recoveryPassword.setDate(new Date());
        entityManager.persist(recoveryPassword);
    }
}
