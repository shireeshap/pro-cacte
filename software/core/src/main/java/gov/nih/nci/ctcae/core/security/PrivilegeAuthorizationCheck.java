package gov.nih.nci.ctcae.core.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.context.SecurityContextHolder;

/**
 * @author Vinay Kumar
 * @since Mar 2, 2009
 */
public class PrivilegeAuthorizationCheck {
    protected static final Log logger = LogFactory.getLog(PrivilegeAuthorizationCheck.class);

    private AccessDecisionManager accessDecisionManager;

    public boolean authorize(final String privelege) throws AccessDeniedException {


        ConfigAttributeDefinition configAttributeDefinition = new ConfigAttributeDefinition(privelege);

        return authorize(configAttributeDefinition);

    }

    public boolean authorize(ConfigAttributeDefinition configAttributeDefinition) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (configAttributeDefinition != null) {
            try {
                accessDecisionManager.decide(authentication, null, configAttributeDefinition);
                return true;
            } catch (AccessDeniedException e) {
                logger.debug(String.format("user %s does not have permission  %s", authentication.getName(),
                        configAttributeDefinition.getConfigAttributes()));
            }
        } else {
            logger.debug(String.format("returning false because permission %s is null",
                    configAttributeDefinition.getConfigAttributes()));
        }

        return false;

    }

    @Required
    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

}
