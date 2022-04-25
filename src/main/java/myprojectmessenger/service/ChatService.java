package myprojectmessenger.service;

import myprojectmessenger.dao.ChatDao;
import myprojectmessenger.dao.UserDao;
import myprojectmessenger.entity.Chat;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.ChatDTO;
import myprojectmessenger.model.ChatModel;
import myprojectmessenger.model.ChatType;
import myprojectmessenger.model.RecentChats;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class ChatService {
    private final ChatDao chatDao;
    private final UserDao userDao;

    public ChatService(ChatDao chatDao, UserDao userDao) {
        this.chatDao = chatDao;
        this.userDao = userDao;
    }

    public RecentChats addChat(String sessionId,
                               ChatModel chatModel) {
        Chat chat = saveChat(sessionId, chatModel.getUserIds(), chatModel.getTitle(), chatModel.getType());
        RecentChats recentChats = new RecentChats();
        recentChats.getChats().add(convertToDto(chat));
        return recentChats;
    }

    private Chat saveChat(String sessionId, Collection<Long> userIds, String title, ChatType type) {
        Chat chat = new Chat();
        Set<User> users = new HashSet<>();
        users.add(userDao.findUserBySessionId(sessionId));
        users.addAll(userDao.findUsersByIds(userIds));
        chat.setAuthor(userDao.findUserBySessionId(sessionId));
        chat.setCreated(new Date());
        chat.setUsers(users);
        chat.setTitle(title);
        chat.setType(type);
        chatDao.addChat(chat);
        return chat;
    }

    public RecentChats getRecentChats(String sessionId) {
        RecentChats recentChats = new RecentChats();
        Set<Chat> chats = chatDao.findRecent10ChatsForUser(userDao.findUserBySessionId(sessionId));
        for (Chat chat : chats) {
            ChatDTO chatDTO = convertToDto(chat);
            recentChats.getChats().add(chatDTO);
        }
        return recentChats;

    }

    private ChatDTO convertToDto(Chat chat) {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setTitle(chat.getTitle());
        chatDTO.setId(chat.getId());
        return chatDTO;
    }
}
