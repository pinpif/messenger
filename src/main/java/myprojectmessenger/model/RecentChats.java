package myprojectmessenger.model;

import java.util.ArrayList;
import java.util.List;

public class RecentChats {
    private List<ChatDTO> chats = new ArrayList<>();

    public List<ChatDTO> getChats() {
        return chats;
    }

    public void setChats(List<ChatDTO> chats) {
        this.chats = chats;
    }
}
