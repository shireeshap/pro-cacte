package gov.nih.nci.ctcae.web.security;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.Authentication;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Required;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.UserRepository;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Feb 1, 2010
 * Time: 2:04:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationListener implements org.springframework.context.ApplicationListener {
    UserRepository userRepository;

    public void onApplicationEvent(ApplicationEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            User user = (User) auth.getPrincipal();
            if (!user.isAccountNonLocked()) {
                auth.setAuthenticated(false);
            } else {
                user.setNumberOfAttempts(0);
            }
        }
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
