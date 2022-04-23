package myprojectmessenger.controllers;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.dao.ChatDao;
import myprojectmessenger.dao.MessageDao;
import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.Message;
import myprojectmessenger.entity.User;
import myprojectmessenger.exception.BlockedByUserException;
import myprojectmessenger.exception.NotInChatException;
import myprojectmessenger.model.ContentDto;
import myprojectmessenger.model.MessageDto;
import myprojectmessenger.model.MessageModel;
import myprojectmessenger.model.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class MessageController {
    private final MessageDao messageDao;
    private final ChatDao chatDao;
    private final AuthorizationDao authorizationDao;

    public MessageController(MessageDao messageDao, ChatDao chatDao, AuthorizationDao authorizationDao) {
        this.messageDao = messageDao;
        this.chatDao = chatDao;
        this.authorizationDao = authorizationDao;
    }

    @GetMapping("/api/chat/{chatId}/message")
    public Messages getMessage(@RequestHeader(value = "SessionId") String sessionId,
                               @PathVariable Long chatId,
                               @RequestParam(required = false, defaultValue = "50") Integer limit) {
        Messages messages = new Messages();
        for (Message message : messageDao.getMessages(chatDao.findById(chatId), authorizationDao.findUserBySessionId(sessionId), limit)) {
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

    @PostMapping("/api/chat/{chatId}/message")
    public void addMessage(@RequestHeader(value = "SessionId") String sessionId,
                           @RequestBody MessageModel messageModel,
                           @PathVariable Long chatId) {
        User user = authorizationDao.findUserBySessionId(sessionId);
        Date date = messageModel.getDate();
        String text = messageModel.getText();
        Chat chat = chatDao.findById(chatId);
        messageDao.addMessage(chat, text, date, user);
    }

    @ExceptionHandler({BlockedByUserException.class, NotInChatException.class})
    public ResponseEntity<Void> handleIllegalArgumentException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
