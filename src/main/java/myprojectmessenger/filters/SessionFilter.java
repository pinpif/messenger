package myprojectmessenger.filters;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.service.SessionService;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionFilter implements Filter {
    private final AuthorizationDao authorizationDao;

    public SessionFilter(AuthorizationDao authorizationDao) {
        this.authorizationDao = authorizationDao;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String sessionId = httpServletRequest.getHeader(SessionService.SESSION_HEADER_NAME);
        if (sessionId == null || !authorizationDao.sessionExist(sessionId)) {
            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }


    }
}
