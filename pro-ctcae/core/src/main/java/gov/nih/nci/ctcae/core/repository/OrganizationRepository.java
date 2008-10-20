package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
@Transactional(readOnly = true,propagation = Propagation.REQUIRED)
public class OrganizationRepository extends AbstractRepository<Organization, OrganizationQuery> {

    @Override
    protected Class<Organization> getPersistableClass() {
        return Organization.class;

    }
}
