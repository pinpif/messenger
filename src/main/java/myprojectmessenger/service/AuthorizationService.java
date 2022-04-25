package myprojectmessenger.service;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.SessionInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class AuthorizationService {
    private final AuthorizationDao authorizationDao;

    public AuthorizationService(AuthorizationDao authorizationDao) {
        this.authorizationDao = authorizationDao;
    }

    public SessionInfo authorization(String authorization) {
        byte[] usernamePasswordByteArray = Base64Utils.decodeFromString(authorization.substring("Basic ".length()));
        String usernamePassword = new String(usernamePasswordByteArray, StandardCharsets.UTF_8);
        String[] usernameAndPassword = usernamePassword.split(":");
        User user = authorizationDao.findUserByLoginAndPassword(usernameAndPassword[0], usernameAndPassword[1]);
        UUID uuid = UUID.randomUUID();
        authorizationDao.addSession(user, uuid);
        return new SessionInfo(user, uuid);
    }

    public void logout(String sessionId) {
        authorizationDao.logout(sessionId);
    }


}
