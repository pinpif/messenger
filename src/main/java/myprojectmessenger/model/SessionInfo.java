package myprojectmessenger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myprojectmessenger.entity.User;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionInfo {
    private User user;
    private UUID uuid;
}
