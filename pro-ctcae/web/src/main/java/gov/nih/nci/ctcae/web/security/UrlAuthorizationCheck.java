package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.security.PrivilegeAuthorizationCheck;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.intercept.web.DefaultFilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.FilterSecurityInterceptor;


/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class UrlAuthorizationCheck implements ApplicationContextAware {

    protected static final Log logger = LogFactory.getLog(UrlAuthorizationCheck.class);

    private ApplicationContext applicationContext;

    private PrivilegeAuthorizationCheck privilegeAuthorizationCheck;

    public boolean authorize(final String url) throws AccessDeniedException {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor) applicationContext.getBean("_filterSecurityInterceptor");

        DefaultFilterInvocationDefinitionSource objectDefinitionSource = (DefaultFilterInvocationDefinitionSource) filterSecurityInterceptor.getObjectDefinitionSource();


        ConfigAttributeDefinition configAttributeDefinition = objectDefinitionSource.lookupAttributes(url, "");

        if (configAttributeDefinition != null) {
            if (privilegeAuthorizationCheck.authorize(configAttributeDefinition)) {
                return true;
            } else {
                logger.error(String.format("user %s can not access url:%s because required permission is %s", authentication.getName(),
                        url, configAttributeDefinition.getConfigAttributes()));
            }
        } else {
            logger.error(String.format("user %s can not access url:%s because no security have been applied for this url", authentication.getName(), url));
        }

        return false;

    }

    
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Required
    public void setPrivilegeAuthorizationCheck(PrivilegeAuthorizationCheck privilegeAuthorizationCheck) {
        this.privilegeAuthorizationCheck = privilegeAuthorizationCheck;
    }
}