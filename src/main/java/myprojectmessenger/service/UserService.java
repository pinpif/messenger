package myprojectmessenger.service;

import myprojectmessenger.dao.UserDao;
import myprojectmessenger.entity.Account;
import myprojectmessenger.entity.User;
import myprojectmessenger.entity.UserStatus;
import myprojectmessenger.model.BlockUser;
import myprojectmessenger.model.RegistrationRequest;
import myprojectmessenger.model.UserSearch;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(RegistrationRequest registrationRequest) {
        UserStatus userStatus = new UserStatus();
        userStatus.setDate(new Date());
        userStatus.setState(false);

        Account account = new Account();
        account.setLogin(registrationRequest.getAccountDto().getLogin());
        account.setPassword(registrationRequest.getAccountDto().getPassword());
        account.setStatus(true);
        account.setRegistrationDate(new Date());

        User user = new User();
        user.setName(registrationRequest.getUserDto().getName());
        user.setAge(registrationRequest.getUserDto().getAge());
        user.setAccount(account);
        user.setStatus(userStatus);
        userDao.createUser(user);
    }

    public List<UserSearch> findUsers(String searchString, int limit) {
        return userDao.findUsers(searchString, limit)
                .stream()
                .map(this::convertToSearchUser)
                .collect(Collectors.toList());
    }

    private UserSearch convertToSearchUser(User user) {
        UserSearch userSearch = new UserSearch();
        userSearch.setId(user.getId());
        userSearch.setName(user.getName());
        userSearch.setLogin(user.getAccount().getLogin());
        return userSearch;
    }

    public void blockUser(String sessionId, BlockUser blockUser) {
        User user = userDao.findUserBySessionId(sessionId);
        User otherUser = userDao.findUserById(blockUser.getBlockUserId());
        userDao.blockUser(user, otherUser);
    }

    public void unblockUser(String sessionId, BlockUser blockUser) {
        User user = userDao.findUserBySessionId(sessionId);
        User otherUser = userDao.findUserById(blockUser.getBlockUserId());
        userDao.unblockUser(user, otherUser);
    }
}
