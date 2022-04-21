package myprojectmessenger.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.entity.User;
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

    // @RequestBody
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
        authorizationDao.addSession(user,uuid);
        response.setHeader("SessionId", uuid.toString());
        return new SessionInfo(user, uuid);
    }
@Transactional
@DeleteMapping("/api/logout")
public void logout(@RequestHeader(value = "SessionId") String sessionId){
        authorizationDao.logout(sessionId);
}

    // Добавить таблицу, содержащую информацию о статусе сообщения (прочитано или не прочитано)
    // Добавить таблицу контактов пользователя
    // Добавить метод добавления пользователя в контакты
    // Добавить метод поиска пользователей по логину/имени
    // Добавить таблицу заблокированных пользователей
    // Добавить метод блокирования пользователя
    // Добавить метод разблокирования пользователя
    // Добавить метод получения последних N (50) сообщений в чате

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class SessionInfo {
        private User user;
        private UUID uuid;
    }
}
