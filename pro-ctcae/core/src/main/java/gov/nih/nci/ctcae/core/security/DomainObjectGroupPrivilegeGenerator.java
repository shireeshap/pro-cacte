package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;

/**
 * @author Vinay Kumar
 * @since Mar 10, 2009
 */
public class DomainObjectGroupPrivilegeGenerator {
    protected final String GROUP = "GROUP";

    public String generatePrivilege(Persistable persistable) {
        if (StudyOrganization.class.isAssignableFrom(persistable.getClass())) {
            return getPrivilege(StudyOrganization.class);
        }
        return getPrivilege(persistable.getClass());
    }

    private String getPrivilege(final Class<? extends Persistable> aClass) {
        return aClass.getName() + "." + GROUP;
    }
}