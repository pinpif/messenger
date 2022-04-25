package myprojectmessenger.controllers;

import myprojectmessenger.exception.BlockedByUserException;
import myprojectmessenger.exception.NotInChatException;
import myprojectmessenger.model.MessageModel;
import myprojectmessenger.model.Messages;
import myprojectmessenger.service.MessageService;
import myprojectmessenger.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {
    private final MessageService messageService;


    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/api/chat/{chatId}/message")
    public Messages getMessage(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId,
                               @PathVariable Long chatId,
                               @RequestParam(required = false, defaultValue = "50") Integer limit) {
        return messageService.getMessage(sessionId, chatId, limit);
    }

    @PostMapping("/api/chat/{chatId}/message")
    public void addMessage(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId,
                           @RequestBody MessageModel messageModel,
                           @PathVariable Long chatId) {
        messageService.addMessage(sessionId, messageModel, chatId);
    }

    @ExceptionHandler({BlockedByUserException.class, NotInChatException.class})
    public ResponseEntity<Void> handleIllegalArgumentException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
