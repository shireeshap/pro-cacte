package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

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
                if (user.getNumberOfAttempts() > 0) {
                    user.setNumberOfAttempts(0);
                    userRepository.save(user);
                    if(!auth.isAuthenticated()){
                        String password = (String) auth.getCredentials();
                        userRepository.setCheckAccountLockout(false);
                        user = userRepository.loadUserByUsername(user.getUsername());
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(token);
                        userRepository.setCheckAccountLockout(true);
                    }
                }
            }
        }
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
