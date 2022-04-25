package myprojectmessenger.controllers;

import myprojectmessenger.dao.ChatDao;
import myprojectmessenger.dao.UserDao;
import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.User;
import myprojectmessenger.exception.ChatExists;
import myprojectmessenger.model.ChatDTO;
import myprojectmessenger.model.ChatModel;
import myprojectmessenger.model.RecentChats;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatDao chatDao;
    private final UserDao userDao;

    public ChatController(ChatDao chatDao, UserDao userDao) {
        this.chatDao = chatDao;
        this.userDao = userDao;
    }

    @GetMapping
    public RecentChats chats(@RequestHeader(value = "SessionId") String sessionId) {
        RecentChats recentChats = new RecentChats();
        Set<Chat> chats = chatDao.findRecent10ChatsForUser(userDao.findUserBySessionId(sessionId));
        for (Chat chat : chats) {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setTitle(chat.getTitle());
            chatDTO.setId(chat.getId());
            recentChats.getChats().add(chatDTO);
        }
        return recentChats;

    }

    @PostMapping
    public RecentChats addChat(@RequestHeader(value = "SessionId") String sessionId,
                               @RequestBody ChatModel chatModel) {
        Chat chat = new Chat();
        Set<User> users = new HashSet<>();
        users.add(userDao.findUserBySessionId(sessionId));
        users.addAll(userDao.findUsersByIds(chatModel.getUserIds()));
        chat.setAuthor(userDao.findUserBySessionId(sessionId));
        chat.setCreated(new Date());
        chat.setUsers(users);
        chat.setTitle(chatModel.getTitle());
        chat.setType(chatModel.getType());
        chatDao.addChat(chat);
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setTitle(chat.getTitle());
        RecentChats recentChats = new RecentChats();
        recentChats.getChats().add(chatDTO);
        return recentChats;
    }

    @ExceptionHandler({ChatExists.class})
    public ResponseEntity<Void> handleIllegalArgumentException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
