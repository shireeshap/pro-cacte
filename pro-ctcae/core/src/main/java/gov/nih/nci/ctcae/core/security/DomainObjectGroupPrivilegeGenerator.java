package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Persistable;

/**
 * @author Vinay Kumar
 * @crated Mar 10, 2009
 */
public class DomainObjectGroupPrivilegeGenerator {
    protected final String GROUP = "GROUP";

    public String generatePrivilege(Persistable persistable) {
        return persistable.getClass().getName() + "." + GROUP;
    }
}