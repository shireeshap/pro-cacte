package gov.nih.nci.ctcae.core.security.afterinvocation;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.security.DomainObjectAuthorizationCheck;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.afterinvocation.AfterInvocationProvider;

/**
 * @author Vinay Kumar
 * @since Mar 10, 2009
 */
public abstract class AbstractAfterInvocationProvider implements AfterInvocationProvider {

    protected DomainObjectAuthorizationCheck domainObjectAuthorizationCheck;
    protected final Log logger = LogFactory.getLog(getClass());
    private Class processDomainObjectClass = Persistable.class;

    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    /**
     * This implementation supports any type of class, because it does not query the presented secure object.
     *
     * @param clazz the secure object
     * @return always <code>true</code>
     */
    public boolean supports(Class clazz) {
        return true;
    }

    public Class getProcessDomainObjectClass() {
        return processDomainObjectClass;
    }

    @Required
    public void setDomainObjectAuthorizationCheck(DomainObjectAuthorizationCheck domainObjectAuthorizationCheck) {
        this.domainObjectAuthorizationCheck = domainObjectAuthorizationCheck;
    }
}
