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
import myprojectmessenger.model.MessagesModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public MessagesModel getMessage(String sessionId, Long chatId, Integer limit) {
        Chat chat = chatDao.findById(chatId);
        User user = userDao.findUserBySessionId(sessionId);
        List<Message> messages = messageDao.getMessages(chat, user, limit);
        return convertToModel(messages);
    }

    private MessagesModel convertToModel(List<Message> messages) {
        MessagesModel messagesModel = new MessagesModel();
        for (Message message : messages) {
            MessageDto messageDto = convertToMessageDto(message);
            messagesModel.getMessages().add(messageDto);
        }
        return messagesModel;
    }

    private MessageDto convertToMessageDto(Message message) {
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
            ContentDto contentDto = convertToContentDto(message);
            messageDto.setContentDto(contentDto);
        }
        return messageDto;
    }

    private ContentDto convertToContentDto(Message message) {
        ContentDto contentDto = new ContentDto();
        contentDto.setFilename(message.getContent().getName());
        contentDto.setContent(message.getContent().getContent());
        return contentDto;
    }
}
