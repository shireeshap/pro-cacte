package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */

@Transactional(readOnly = true,propagation = Propagation.REQUIRED)
public class StudyOrganizationRepository extends AbstractRepository<StudyOrganization, StudyOrganizationQuery> {

    @Override
    protected Class<StudyOrganization> getPersistableClass() {
        return StudyOrganization.class;

    }
    
    public ArrayList<Organization> findStudySites(){
    	StudyOrganizationQuery query = new StudyOrganizationQuery("select distinct o.organization from StudySite o order by o.organization.name ");
    	return new ArrayList<Organization>((Collection<Organization>)genericRepository.find(query));
    }
    
}