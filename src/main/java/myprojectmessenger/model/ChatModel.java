package myprojectmessenger.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ChatModel {
    private ChatType type;
    private String title;
    private Set<Long> userIds;
}
