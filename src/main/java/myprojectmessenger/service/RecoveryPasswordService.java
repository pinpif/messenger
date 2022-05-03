package myprojectmessenger.service;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.dao.MailDao;
import myprojectmessenger.entity.Account;
import myprojectmessenger.entity.RecoveryPassword;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Component
public class RecoveryPasswordService {
    private final JavaMailSender emailSender;
    private final MailDao mailDao;
    private final AuthorizationDao authorizationDao;

    public RecoveryPasswordService(JavaMailSender emailSender, MailDao mailDao, AuthorizationDao authorizationDao) {
        this.emailSender = emailSender;
        this.mailDao = mailDao;
        this.authorizationDao = authorizationDao;
    }

    public void sendSimpleEmail(String mail) throws MessagingException {
        UUID uuid = UUID.randomUUID();
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText("<html>" +
                "<body>" +
                "<a href=\"http://localhost:8080/newPassword/" + uuid + "\">ссылка на востановление пароля</a>" +
                "</body>" +
                "</html>", true);
        helper.setTo(mail);
        helper.setSubject("If you want to change password, then click link");
        helper.setFrom("lava.message.mail@gmail.com");
        this.emailSender.send(mimeMessage);

        Account account = authorizationDao.findUserByMail(mail);
        mailDao.saveSession(account, uuid.toString());
    }

    public void addNewPassword(String uuid, String password) {
        RecoveryPassword recoveryPassword = mailDao.findRecoveryPasswordByCode(uuid);
        mailDao.changePassword(recoveryPassword.getAccount(), password);
    }
}
