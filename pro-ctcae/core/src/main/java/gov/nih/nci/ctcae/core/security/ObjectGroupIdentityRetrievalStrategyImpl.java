package gov.nih.nci.ctcae.core.security;

import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.objectidentity.ObjectIdentityImpl;

/**
 * @author Vinay Kumar
 * @crated Feb 5, 2009
 */
public class ObjectGroupIdentityRetrievalStrategyImpl implements ObjectGroupIdentityRetrievalStrategy {

    //~ Methods ========================================================================================================

    public ObjectGroupIdentity getObjectGroupIdentity(Object domainObject) {
        return new ObjectGroupIdentityImpl(domainObject);
    }
}