package myprojectmessenger.controllers;

import myprojectmessenger.model.BlockUser;
import myprojectmessenger.model.UserSearch;
import myprojectmessenger.service.SessionService;
import myprojectmessenger.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserSearch> searchUser(@RequestParam String searchString,
                                       @RequestParam(value = "limit", defaultValue = "20", required = false) int limit) {
        return userService.searchUser(searchString, limit);
    }

    @PostMapping("/block")
    public void userBlock(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId,
                          @RequestBody BlockUser blockUser) {
        userService.userBlock(sessionId, blockUser);
    }

    @DeleteMapping("/unblock")
    public void unblockUser(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId,
                            @RequestBody BlockUser blockUser) {
        userService.unblockUser(sessionId, blockUser);
    }
}
