package myprojectmessenger.controllers;

import myprojectmessenger.model.SessionInfo;
import myprojectmessenger.service.AuthorizationService;
import myprojectmessenger.service.SessionService;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public SessionInfo authorization(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            HttpServletResponse response) {
        SessionInfo sessionInfo = authorizationService.authorization(authorization);
        response.setHeader(SessionService.SESSION_HEADER_NAME, sessionInfo.getUuid().toString());
        return sessionInfo;
    }

    @Transactional
    @DeleteMapping("/api/logout")
    public void logout(@RequestHeader(value = SessionService.SESSION_HEADER_NAME) String sessionId) {
        authorizationService.logout(sessionId);
    }


}
