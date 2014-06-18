package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.security.PrivilegeAuthorizationCheck;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.SecurityConfig;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.intercept.web.DefaultFilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.FilterSecurityInterceptor;


/**
 * @author Vinay Kumar
 * @since Feb 26, 2009
 */
public class UrlAuthorizationCheck implements ApplicationContextAware {

    protected static final Log logger = LogFactory.getLog(UrlAuthorizationCheck.class);

    private ApplicationContext applicationContext;

    private PrivilegeAuthorizationCheck privilegeAuthorizationCheck;
    private AuthorizationServiceImpl authorizationServiceImpl;
    private Map<String, Boolean> filteredAccessPrivilege;

    public boolean authorize(final String url, final String objectId) throws AccessDeniedException {
    	User user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof User){
        	user = (User) authentication.getPrincipal();
        }
        
        FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor) applicationContext.getBean("_filterSecurityInterceptor");

        DefaultFilterInvocationDefinitionSource objectDefinitionSource = (DefaultFilterInvocationDefinitionSource) filterSecurityInterceptor.getObjectDefinitionSource();

        ConfigAttributeDefinition configAttributeDefinition = objectDefinitionSource.lookupAttributes(url, "");
        
        /* Append objectId to the privilegeName, so that the required privilege is specific to the current objectInstance 
         * being accessed. 
         */
        if(!StringUtils.isEmpty(objectId) && user != null && !user.isAdmin()){
        	List<String> privilegeList = new ArrayList<String>();
        	filteredAccessPrivilege = authorizationServiceImpl.getFilteredAccessPrivilegeMap();
        	Collection configAttributes = configAttributeDefinition.getConfigAttributes();
        	Iterator itr = configAttributes.iterator();
        	while(itr.hasNext()){
        		SecurityConfig securityConfig = (SecurityConfig) itr.next();
                String privilegeName = securityConfig.getAttribute();
                if(filteredAccessPrivilege.get(privilegeName) != null){
                	privilegeList.add(privilegeName + objectId);
                } else {
                	privilegeList.add(privilegeName);
                }
        	}
        	configAttributeDefinition = new ConfigAttributeDefinition(privilegeList.toArray(new String[privilegeList.size()]));
        }

        if (configAttributeDefinition != null) {
            if (privilegeAuthorizationCheck.authorize(configAttributeDefinition)) {
                return true;
            } else {
                logger.debug(String.format("user %s can not access url:%s because required permission is %s", authentication.getName(),
                        url, configAttributeDefinition.getConfigAttributes()));
            }
        } else {
            logger.debug(String.format("user %s can not access url:%s because no security have been applied for this url", authentication.getName(), url));
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
    
    @Required
    public void setAuthorizationServiceImpl(AuthorizationServiceImpl authorizationServiceImpl) {
        this.authorizationServiceImpl = authorizationServiceImpl;
    }
}