package myprojectmessenger.controllers;

import myprojectmessenger.dao.UserDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.BlockUser;
import myprojectmessenger.model.UserSearch;
import myprojectmessenger.service.SessionService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping
    public List<UserSearch> searchUser(@RequestParam String searchString,
                                       @RequestParam(value = "limit", defaultValue = "20", required = false) int limit) {
        List<UserSearch> userSearches = new ArrayList<>();
        for (User user : userDao.findUsers(searchString, limit)) {
            UserSearch userSearch = new UserSearch();
            userSearch.setId(user.getId());
            userSearch.setName(user.getName());
            userSearch.setLogin(user.getAccount().getLogin());
            userSearches.add(userSearch);
        }
        return userSearches;
    }

    @PostMapping("/block")
    public void userBlock(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId,
                          @RequestBody BlockUser blockUser) {
        User user = userDao.findUserBySessionId(sessionId);
        User block = userDao.findUserById(blockUser.getBlockUserId());
        userDao.blockUser(user, block);
    }

    @DeleteMapping("/unblock")
    public void unblockUser(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId,
                            @RequestBody BlockUser blockUser) {
        User user = userDao.findUserBySessionId(sessionId);
        User block = userDao.findUserById(blockUser.getBlockUserId());
        userDao.unblockUser(user, block);
    }
}
