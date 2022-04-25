package myprojectmessenger.service;

import myprojectmessenger.dao.UserDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.BlockUser;
import myprojectmessenger.model.UserSearch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<UserSearch> searchUser(String searchString, int limit) {
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

    public void userBlock(String sessionId, BlockUser blockUser) {
        userDao.blockUser(userDao.findUserBySessionId(sessionId),
                userDao.findUserById(blockUser.getBlockUserId()));
    }

    public void unblockUser(String sessionId, BlockUser blockUser) {
        userDao.unblockUser(userDao.findUserBySessionId(sessionId),
                userDao.findUserById(blockUser.getBlockUserId()));
    }
}
