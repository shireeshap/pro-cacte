package gov.nih.nci.ctcae.core.security.afterinvocation;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.User;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Vinay Kumar
 * @since Mar 10, 2009
 */
public class DomainObjectCollectionAfterInvocationProvider extends AbstractAfterInvocationProvider {


    public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config, Object returnedObject) throws AccessDeniedException {

        if(((User)authentication.getPrincipal()).isAdmin()){
            return returnedObject;
        }
        if (returnedObject == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Return object is null, skipping");
            }

            return null;
        }

        // Need to process the Collection for this invocation
        Filterer filterer;

        if (returnedObject instanceof Collection) {
            filterer = new CollectionFilterer((Collection) returnedObject);
        } else if (returnedObject.getClass().isArray()) {
            filterer = new ArrayFilterer((Object[]) returnedObject);
        } else {
            logger.debug("A Collection or an array (or null) was required as the returnedObject, but the returnedObject was: " + returnedObject + " so returning it");
            return returnedObject;
        }

        // Locate unauthorised Collection elements
        Iterator collectionIter = filterer.iterator();

        while (collectionIter.hasNext()) {
            Object domainObject = collectionIter.next();

            // Ignore nulls or entries which aren't instances of the configured domain object class

            if (domainObject == null || !getProcessDomainObjectClass().isAssignableFrom(domainObject.getClass())) {
                logger.debug(String.format("entry %s in the collection  is not supported for authorization, skipping", domainObject.getClass().getName()));

                continue;
            }

            Persistable persistable = (Persistable) domainObject;
            if (!domainObjectAuthorizationCheck.authorize(authentication, persistable)) {
                filterer.remove(domainObject);

                logger.debug("Principal is NOT authorised for element: " + persistable.getClass() + "." + persistable.getId());
            }
        }

        return filterer.getFilteredObject();


    }


}