package myprojectmessenger.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.service.SessionService;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
public class AuthorizationController {
    private final AuthorizationDao authorizationDao;

    public AuthorizationController(AuthorizationDao authorizationDao) {
        this.authorizationDao = authorizationDao;
    }

    @PostMapping("/login")
    public SessionInfo authorization(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            HttpServletResponse response) {
        System.out.println(authorization);
        byte[] usernamePasswordByteArray = Base64Utils.decodeFromString(authorization.substring("Basic ".length()));
        String usernamePassword = new String(usernamePasswordByteArray, StandardCharsets.UTF_8);
        System.out.println(usernamePassword);
        String[] usernameAndPassword = usernamePassword.split(":");
        User user = authorizationDao.findUserByLoginAndPassword(usernameAndPassword[0], usernameAndPassword[1]);
        UUID uuid = UUID.randomUUID();
        authorizationDao.addSession(user, uuid);
        response.setHeader(SessionService.SESSION_HEADER_NAME, uuid.toString());
        return new SessionInfo(user, uuid);
    }

    @Transactional
    @DeleteMapping("/api/logout")
    public void logout(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId) {
        authorizationDao.logout(sessionId);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SessionInfo {
        private User user;
        private UUID uuid;
    }
}
