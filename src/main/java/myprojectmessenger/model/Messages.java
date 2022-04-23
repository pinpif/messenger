package myprojectmessenger.model;

import java.util.ArrayList;
import java.util.List;

public class Messages {
    private List<MessageDto> messages = new ArrayList<>();

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }
}
