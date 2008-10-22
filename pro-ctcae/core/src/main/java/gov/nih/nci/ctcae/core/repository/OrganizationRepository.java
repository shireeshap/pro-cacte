package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class OrganizationRepository extends
		AbstractRepository<Organization, OrganizationQuery> {

	@Override
	protected Class<Organization> getPersistableClass() {
		return Organization.class;

	}

	public List<Study> findStudiesForOrganization(String organizationName) {
		OrganizationQuery organizationQuery = new OrganizationQuery();
		organizationQuery.filterByOrganizationName(organizationName);
		Organization organization = findSingle(organizationQuery);
		Iterator<StudyOrganization> studyOrganizations = organization
				.getStudyOrganizations().iterator();
		
		ArrayList<Study> studies = new ArrayList<Study>();
		while(studyOrganizations.hasNext()){
			studies.add(studyOrganizations.next().getStudy());
		}
		return studies;
	}
}
