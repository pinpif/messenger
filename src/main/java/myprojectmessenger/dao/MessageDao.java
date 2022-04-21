package myprojectmessenger.dao;

import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.Message;
import myprojectmessenger.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

@Component
public class MessageDao {
    private final EntityManager entityManager;

    public MessageDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Transactional
    public void addMessage(Chat chat, String text, Date date, User user){
        Message message =new Message();
        message.setDate(date);
        message.setChat(chat);
        message.setAuthor(user);
        message.setMessage(text);
        entityManager.persist(message);
    }
    public void addFile(Chat chat,String fileName, byte[] bytes){

    }
    public void showMessage(){

    }
}
