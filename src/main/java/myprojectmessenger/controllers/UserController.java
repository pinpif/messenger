package myprojectmessenger.controllers;

import myprojectmessenger.model.BlockUser;
import myprojectmessenger.model.RegistrationRequest;
import myprojectmessenger.model.UserSearch;
import myprojectmessenger.service.SessionService;
import myprojectmessenger.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public void createUser(@RequestBody RegistrationRequest registrationRequest) {
        userService.createUser(registrationRequest);
    }

    @GetMapping("/api/users")
    public List<UserSearch> findUsers(@RequestParam String searchString,
                                      @RequestParam(value = "limit", defaultValue = "20", required = false) int limit) {
        return userService.findUsers(searchString, limit);
    }

    @PostMapping("/api/users/block")
    public void blockUser(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId,
                          @RequestBody BlockUser blockUser) {
        userService.blockUser(sessionId, blockUser);
    }

    @DeleteMapping("/api/users/unblock")
    public void unblockUser(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId,
                            @RequestBody BlockUser blockUser) {
        userService.unblockUser(sessionId, blockUser);
    }
}
