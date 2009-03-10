package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.Study;

/**
 * @author Vinay Kumar
 * @crated Mar 10, 2009
 */
public class DomainObjectPrivilegeGenerator {

    public String generatePrivilege(Persistable persistable) {
        if (CRF.class.isAssignableFrom(persistable.getClass())) {
            CRF crf = (CRF) persistable;
            return generatePrivilege(crf.getStudy());


        } else if (Study.class.isAssignableFrom(persistable.getClass())) {

            return generatePrivilegeForPersistable(persistable);
        }
        return generatePrivilegeForPersistable(persistable);
    }

    private String generatePrivilegeForPersistable(Persistable persistable) {
        return persistable.getClass().getName() + "." + persistable.getId();
    }
}
