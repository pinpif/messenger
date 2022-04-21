package myprojectmessenger;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.filters.SessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfiguration {

    @Bean
    public FilterRegistrationBean<SessionFilter> sessionFilter(AuthorizationDao authorizationDao) {
        FilterRegistrationBean<SessionFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new SessionFilter(authorizationDao));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
