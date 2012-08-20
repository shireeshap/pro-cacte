package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.User;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

/**
 * The Class ApplicationSecurityManager.
 *
 * @author
 */
public class ApplicationSecurityManager {

    public static User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? (User) authentication.getPrincipal() : null;

    }
}
