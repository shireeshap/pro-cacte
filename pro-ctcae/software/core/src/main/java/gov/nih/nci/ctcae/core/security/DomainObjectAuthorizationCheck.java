package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.Organization;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Vinay Kumar
 * @since Mar 10, 2009
 */
public class DomainObjectAuthorizationCheck {

    private DomainObjectPrivilegeGenerator privilegeGenerator;
    private DomainObjectGroupPrivilegeGenerator groupPrivilegeGenerator;
    private static final Log logger = LogFactory.getLog(DomainObjectAuthorizationCheck.class);
    private List<? extends Persistable> ignoredClasses = new ArrayList<Persistable>();

    public DomainObjectAuthorizationCheck() {
        privilegeGenerator = new DomainObjectPrivilegeGenerator();
        groupPrivilegeGenerator = new DomainObjectGroupPrivilegeGenerator();

    }

    public boolean authorize(Authentication authentication, Persistable persistable) throws AccessDeniedException {

        if (persistable == null) {
            logger.debug(String.format("AfterInvocationProvider will return true for null objects.", persistable));
            return true;
        }
        if (ignoredClasses.contains(persistable.getClass().getName())) {
            logger.debug(String.format("AfterInvocationProvider will not decide for instance level security of %s. " +
                    "Use MethodAuthorizationCheckVoter for instance level security.", persistable.getClass()));

            return true;
        }
        Set<String> privileges = privilegeGenerator.generatePrivilege(persistable);
        for (String privilege : privileges) {
            if (hasPermission(authentication, persistable, privilege)) {
                return true;
            }
        }

        String groupPrivilege = groupPrivilegeGenerator.generatePrivilege(persistable);
        if (hasPermission(authentication, persistable, groupPrivilege)) {
            return true;
        }
        logger.debug(String.format("User %s is not having any of privileges %s or %s  on %s object to access it. So do not return this object.",
                authentication.getName(), privileges, groupPrivilege, persistable.getClass().getName()));
        return false;

    }


    private boolean hasPermission(Authentication authentication, Persistable persistable, String privilege) {
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (StringUtils.equals(grantedAuthority.getAuthority(), privilege)) {
                logger.debug(String.format("User %s is having privilege %s on %s object. So Returning this object.",
                        authentication.getName(), privilege, persistable.getClass().getName()));
                return true;
            }
        }
//        if (persistable.getClass().isAssignableFrom(Organization.class)) {
//            User user = (User) authentication.getPrincipal();
//            if (user.isLeadCRA() || user.isOverallPI()) {
//                return true;
//            }
//        }

        return false;
    }

    @Required
    public void setIgnoredClasses
            (List<? extends Persistable> ignoredClasses) {
        this.ignoredClasses = ignoredClasses;
    }
}