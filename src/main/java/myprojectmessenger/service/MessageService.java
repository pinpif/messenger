package myprojectmessenger.service;

import myprojectmessenger.dao.ChatDao;
import myprojectmessenger.dao.MessageDao;
import myprojectmessenger.dao.UserDao;
import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.Message;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.ContentDto;
import myprojectmessenger.model.MessageDto;
import myprojectmessenger.model.MessageModel;
import myprojectmessenger.model.Messages;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {
    private final MessageDao messageDao;
    private final ChatDao chatDao;
    private final UserDao userDao;

    public MessageService(MessageDao messageDao, ChatDao chatDao, UserDao userDao) {
        this.messageDao = messageDao;
        this.chatDao = chatDao;
        this.userDao = userDao;
    }

    public void addMessage(String sessionId, MessageModel messageModel, Long chatId) {
        User user = userDao.findUserBySessionId(sessionId);
        Date date = messageModel.getDate();
        String text = messageModel.getText();
        Chat chat = chatDao.findById(chatId);
        messageDao.addMessage(chat, text, date, user);
    }

    public Messages getMessage(String sessionId, Long chatId, Integer limit) {
        Messages messages = new Messages();
        for (Message message : messageDao.getMessages(chatDao.findById(chatId), userDao.findUserBySessionId(sessionId), limit)) {
            MessageDto messageDto = new MessageDto();
            messageDto.setDate(message.getDate());
            if (message.getAuthor().getName() != null) {
                messageDto.setAuthor(message.getAuthor().getName());
            } else {
                messageDto.setAuthor(message.getAuthor().getAccount().getLogin());
            }
            messageDto.setAuthorId(message.getAuthor().getId());
            messageDto.setMessage(message.getMessage());
            if (message.getContent() != null) {
                ContentDto contentDto = new ContentDto();
                contentDto.setFilename(message.getContent().getName());
                contentDto.setContent(message.getContent().getContent());
                messageDto.setContentDto(contentDto);
            }
            messages.getMessages().add(messageDto);
        }
        return messages;
    }
}
