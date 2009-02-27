package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.cabig.ctms.web.chrome.Section;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.intercept.web.DefaultFilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.FilterSecurityInterceptor;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class SectionAuthorizationTag extends TagSupport {

    protected static final Log logger = LogFactory.getLog(SectionAuthorizationTag.class);

    //~ Instance fields ================================================================================================

    private Section section;

    //~ Methods ========================================================================================================

    public int doStartTag() throws JspException {

        if ((null == section)) {
            return Tag.SKIP_BODY;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "SecurityContextHolder did not return a non-null Authentication object, so skipping tag body");
            }

            return Tag.SKIP_BODY;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String url = section.getMainUrl();


        ApplicationContext applicationContext = getContext(pageContext);

        FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor) applicationContext.getBeansOfType(FilterSecurityInterceptor.class).values().iterator().next();

        DefaultFilterInvocationDefinitionSource objectDefinitionSource = (DefaultFilterInvocationDefinitionSource) filterSecurityInterceptor.getObjectDefinitionSource();


        ConfigAttributeDefinition configAttributeDefinition = objectDefinitionSource.lookupAttributes(url, "");
        AccessDecisionManager accessDecisionManager = (AccessDecisionManager) applicationContext.getBean("accessDecisionManager");
        try {
            accessDecisionManager.decide(authentication, null, configAttributeDefinition);
            return Tag.EVAL_BODY_INCLUDE;
        } catch (AccessDeniedException e) {
            logger.debug(String.format("user %s can not see url:%s", authentication.getName(), url));
            return Tag.SKIP_BODY;
        }


//
//        if ((acls == null) || (acls.length == 0)) {
//            return Tag.SKIP_BODY;
//        }
//
//        for (int i = 0; i < acls.length; i++) {
//            // Locate processable AclEntrys
//            if (acls[i] instanceof BasicAclEntry) {
//                BasicAclEntry processableAcl = (BasicAclEntry) acls[i];
//
//                // See if principal has any of the required permissions
//                for (int y = 0; y < requiredIntegers.length; y++) {
//                    if (processableAcl.isPermitted(requiredIntegers[y].intValue())) {
//                        if (logger.isDebugEnabled()) {
//                            logger.debug("Including tag body as found permission: " + requiredIntegers[y]
//                                    + " due to AclEntry: '" + processableAcl + "'");
//                        }
//
//                        return Tag.EVAL_BODY_INCLUDE;
//                    }
//                }
//            }
//        }
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("No permission, so skipping tag body");
//        }

    }

    /**
     * Allows test cases to override where application context obtained from.
     *
     * @param pageContext so the <code>ServletContext</code> can be accessed as required by Spring's
     *                    <code>WebApplicationContextUtils</code>
     * @return the Spring application context (never <code>null</code>)
     */
    protected ApplicationContext getContext(PageContext pageContext) {
        ServletContext servletContext = pageContext.getServletContext();

        return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }


}

