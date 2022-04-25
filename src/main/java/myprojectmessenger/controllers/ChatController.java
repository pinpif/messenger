package myprojectmessenger.controllers;

import myprojectmessenger.exception.ChatExists;
import myprojectmessenger.model.ChatModel;
import myprojectmessenger.model.RecentChats;
import myprojectmessenger.service.ChatService;
import myprojectmessenger.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public RecentChats recentChats(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId) {
        return chatService.getRecentChats(sessionId);

    }

    @PostMapping
    public RecentChats addChat(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId,
                               @RequestBody ChatModel chatModel) {
        return chatService.addChat(sessionId, chatModel);
    }

    @ExceptionHandler({ChatExists.class})
    public ResponseEntity<Void> handleIllegalArgumentException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
