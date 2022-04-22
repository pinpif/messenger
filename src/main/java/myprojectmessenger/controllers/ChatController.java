package myprojectmessenger.controllers;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.dao.ChatDao;
import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.ChatDTO;
import myprojectmessenger.model.ChatModel;
import myprojectmessenger.model.RecentChats;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatDao chatDao;
    private final AuthorizationDao authorizationDao;

    public ChatController(ChatDao chatDao, AuthorizationDao authorizationDao) {
        this.chatDao = chatDao;
        this.authorizationDao = authorizationDao;
    }

    @GetMapping
    public RecentChats chats(@RequestHeader(value = "SessionId") String sessionId) {
        RecentChats recentChats = new RecentChats();
        Set<Chat> chats = chatDao.findRecent10ChatsForUser(authorizationDao.findUserBySessionId(sessionId));
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
        users.add(authorizationDao.findUserBySessionId(sessionId));
        users.addAll(authorizationDao.findUsersByIds(chatModel.getUserIds()));
        chat.setAuthor(authorizationDao.findUserBySessionId(sessionId));
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
}
