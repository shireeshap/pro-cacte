package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Persistable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;

/**
 * @author Vinay Kumar
 * @crated Mar 10, 2009
 */
public class DomainObjectAuthorizationCheck {

    private DomainObjectPrivilegeGenerator privilegeGenerator;

    private static final Log logger = LogFactory.getLog(DomainObjectAuthorizationCheck.class);

    private DomainObjectGroupPrivilegeGenerator groupPrivilegeGenerator;

    public DomainObjectAuthorizationCheck() {
        privilegeGenerator = new DomainObjectPrivilegeGenerator();
        groupPrivilegeGenerator = new DomainObjectGroupPrivilegeGenerator();

    }

    public boolean authorize(Authentication authentication, Persistable persistable) throws AccessDeniedException {


        String groupPrivilege = groupPrivilegeGenerator.generatePrivilege(persistable);

        if (hasPermission(authentication, persistable, groupPrivilege)) {
            return true;
        }

        String privilege = privilegeGenerator.generatePrivilege(persistable);
        if (hasPermission(authentication, persistable, privilege)) {
            return true;
        }

        logger.debug(String.format("User %s is not having privilege %s or %s on %s object to access it. So do not return this object.",
                authentication.getName(), groupPrivilege, privilege, persistable.getClass().getName()));
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
        return false;
    }


}