package gov.nih.nci.ctcae.core.security.afterinvocation;

import gov.nih.nci.ctcae.core.domain.Persistable;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;

/**
 * @author Vinay Kumar
 * @crated Mar 10, 2009
 */
public class DomainObjectAfterInvocationProvider extends AbstractAfterInvocationProvider {


    public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config, Object returnedObject) throws AccessDeniedException {


        if (returnedObject == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Return object is null, skipping");
            }

            return null;
        }

        if (!getProcessDomainObjectClass().isAssignableFrom(returnedObject.getClass())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Return object is not applicable for this provider, skipping");
            }

            return returnedObject;
        }


        if (domainObjectAuthorizationCheck.authorize(authentication, (Persistable) returnedObject)) {
            return returnedObject;
        }

        logger.debug("Denying access");

        throw new AccessDeniedException(String.format("Authentication %s has NO permissions to the domain object %s", authentication.getName(), returnedObject));


    }


}
