package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

/**
 * @author mehul
 * Date: 7/7/11
 */
public class SecurityHelper {

    public static boolean isUserSiteIndependent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        boolean isLeadStaff = false;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.SITE_PI) || userRole.getRole().equals(Role.SITE_CRA) || userRole.getRole().equals(Role.NURSE) || userRole.getRole().equals(Role.TREATING_PHYSICIAN)) {
                isLeadStaff = false;
            } else {
                isLeadStaff = true;
            }
        }
        return isLeadStaff;
    }
}
