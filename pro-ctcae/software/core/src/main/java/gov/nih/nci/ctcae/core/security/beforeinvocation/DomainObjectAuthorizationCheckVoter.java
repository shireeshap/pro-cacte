package gov.nih.nci.ctcae.core.security.beforeinvocation;

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.vote.AccessDecisionVoter;

/**
 * @author Vinay Kumar
 * @since Mar 16, 2009
 */
public class DomainObjectAuthorizationCheckVoter implements AccessDecisionVoter {

    protected final Log logger = LogFactory.getLog(getClass());

    //~ Instance fields ================================================================================================

    private String authCheckPrefix = "AUTH_CHECK";


    private List<MethodAuthorizationCheck> methodAuthorizationChecks = new ArrayList<MethodAuthorizationCheck>();

    //~ Methods ========================================================================================================

    public String getAuthCheckPrefix() {
        return authCheckPrefix;
    }


    /**
     * Allows the default auth prefix of <code><AUTH_CHECK/code> to be overridden.
     * May be set to an empty value, although this is usually not desirable.
     *
     * @param authCheckPrefix the new prefix
     */
    public void setAuthCheckPrefix(String authCheckPrefix) {
        this.authCheckPrefix = authCheckPrefix;
    }

    public boolean supports(ConfigAttribute attribute) {
        if ((attribute.getAttribute() != null) && attribute.getAttribute().startsWith(getAuthCheckPrefix())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param clazz the secure object
     * @return always <code>true</code>
     */
    public boolean supports(Class clazz) {
        return true;
    }

    public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
        if (object != null && MethodInvocation.class.isAssignableFrom(object.getClass())) {
            logger.debug("Security invocation attempted for object " + object.getClass().getName());

            for (MethodAuthorizationCheck methodAuthorizationCheck : methodAuthorizationChecks) {
                boolean authorize = methodAuthorizationCheck.authorize(authentication, (MethodInvocation) object);
                if (authorize) {
                    return ACCESS_GRANTED;
                }

            }


            return ACCESS_DENIED;

        }
        return ACCESS_ABSTAIN;
    }

    @Required
    public void setMethodAuthorizationChecks(List<MethodAuthorizationCheck> methodAuthorizationChecks) {
        this.methodAuthorizationChecks = methodAuthorizationChecks;
    }
}
