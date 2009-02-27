package gov.nih.nci.ctcae.web.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.intercept.web.DefaultFilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.FilterSecurityInterceptor;

import javax.servlet.ServletContext;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class UrlAuthorizationCheck implements ApplicationContextAware {

    protected static final Log logger = LogFactory.getLog(UrlAuthorizationCheck.class);

    ServletContext servletContext;
    private ApplicationContext applicationContext;
    private AccessDecisionManager accessDecisionManager;

    public boolean authorize(final String url) throws AccessDeniedException {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor) applicationContext.getBean("_filterSecurityInterceptor");

        DefaultFilterInvocationDefinitionSource objectDefinitionSource = (DefaultFilterInvocationDefinitionSource) filterSecurityInterceptor.getObjectDefinitionSource();


        ConfigAttributeDefinition configAttributeDefinition = objectDefinitionSource.lookupAttributes(url, "");

        if (configAttributeDefinition != null) {
            try {
                accessDecisionManager.decide(authentication, null, configAttributeDefinition);
                return true;
            } catch (AccessDeniedException e) {
                logger.error(String.format("user %s can not access url:%s because required permission is %s", authentication.getName(), url,configAttributeDefinition.getConfigAttributes()));
            }
        } else {
            logger.error(String.format("user %s can not access url:%s because no security have been applied for this url", authentication.getName(), url));
        }

        return false;

    }

    @Required
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Required
    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

}