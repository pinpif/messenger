package myprojectmessenger.controllers;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.dao.ChatDao;
import myprojectmessenger.dao.MessageDao;
import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.User;
import myprojectmessenger.exception.BlockedByUserException;
import myprojectmessenger.model.MessageModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
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

    @ExceptionHandler(BlockedByUserException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(BlockedByUserException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
