package myprojectmessenger.service;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.SessionInfo;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationService {
    private final AuthorizationDao authorizationDao;

    public AuthorizationService(AuthorizationDao authorizationDao) {
        this.authorizationDao = authorizationDao;
    }

    public SessionInfo authorization(String username, String password) {
        User user = authorizationDao.findUserByLoginAndPassword(username, password);
        UUID uuid = UUID.randomUUID();
        authorizationDao.addSession(user, uuid);
        return new SessionInfo(user, uuid);
    }

    public void logout(String sessionId) {
        authorizationDao.logout(sessionId);
    }


}
