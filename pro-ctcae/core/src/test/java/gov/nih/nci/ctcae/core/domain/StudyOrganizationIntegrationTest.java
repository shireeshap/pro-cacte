package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.query.StudySiteQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyOrganizationIntegrationTest extends
		AbstractJpaIntegrationTestCase {

	private OrganizationRepository organizationRepository;
	private StudyRepository studyRepository;
	private StudyOrganizationRepository studyOrganizationRepository;
	private Organization organization;
	private Study study;
	private StudySite studySite;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();

		organization = organizationRepository.find(new OrganizationQuery())
				.iterator().next();
		createStudy(0);
		study = studyRepository.find(new StudyQuery()).iterator().next();

		assertNotNull(organization);
		assertNotNull(study);

		studySite = new StudySite();

		studySite.setOrganization(organization);
		studySite.setStudy(study);
		studyOrganizationRepository.save(studySite);

	}

	public void testSaveStudySite() {

		StudySite myStudySite = (StudySite) studyOrganizationRepository
				.findById(studySite.getId());

		assertNotNull(myStudySite);
		assertEquals(studySite, myStudySite);

	}

	public void testFindStudySites() {

		for (int i = 0; i < 10; i++) {
			createStudy(i);
		}

		for (int i = 0; i < 5; i++) {
			createOrganization(i);
		}

		ArrayList<Organization> organizations = new ArrayList<Organization>(
				organizationRepository.find(new OrganizationQuery()));

		ArrayList<Study> studies = new ArrayList<Study>(studyRepository
				.find(new StudyQuery()));

		int i = 0;
		int j = 0;
		for (Organization organization : organizations) {
			i++;
			j = 0;
			for (Study study : studies) {
				j++;
				StudySite studySite = new StudySite();
				studySite.setStudy(study);
				studySite.setOrganization(organization);
				studyOrganizationRepository.save(studySite);
				if (j > 2 * i)
					break;
			}
		}

		for (i = 6; i < 10; i++) {
			createOrganization(i);
		}

		ArrayList<Organization> studySiteOrganizations = studyOrganizationRepository
				.findStudySiteOrganizations();
		
		OrganizationQuery oQuery = new OrganizationQuery();
		oQuery.filterByNciCodeExactMatch("OrganizationNCI0");
		assertEquals(true, studySiteOrganizations.contains(organizationRepository.findSingle(oQuery)));
		
		oQuery = new OrganizationQuery();
		oQuery.filterByNciCodeExactMatch("OrganizationNCI1");
		assertEquals(true, studySiteOrganizations.contains(organizationRepository.findSingle(oQuery)));
		
		oQuery = new OrganizationQuery();
		oQuery.filterByNciCodeExactMatch("OrganizationNCI2");
		assertEquals(true, studySiteOrganizations.contains(organizationRepository.findSingle(oQuery)));
		
		oQuery = new OrganizationQuery();
		oQuery.filterByNciCodeExactMatch("OrganizationNCI3");
		assertEquals(true, studySiteOrganizations.contains(organizationRepository.findSingle(oQuery)));
		
		oQuery = new OrganizationQuery();
		oQuery.filterByNciCodeExactMatch("OrganizationNCI4");
		assertEquals(true, studySiteOrganizations.contains(organizationRepository.findSingle(oQuery)));

		oQuery = new OrganizationQuery();
		oQuery.filterByNciCodeExactMatch("OrganizationNCI1");
		assertEquals(true, studySiteOrganizations.contains(organizationRepository.findSingle(oQuery)));
		
		oQuery = new OrganizationQuery();
		oQuery.filterByNciCodeExactMatch("OrganizationNCI2");
		assertEquals(true, studySiteOrganizations.contains(organizationRepository.findSingle(oQuery)));
		oQuery = new OrganizationQuery();
		oQuery.filterByNciCodeExactMatch("OrganizationNCI5");
		assertEquals(false, studySiteOrganizations.contains(organizationRepository.findSingle(oQuery)));
		
		oQuery = new OrganizationQuery();
		oQuery.filterByNciCodeExactMatch("OrganizationNCI6");
		assertEquals(false, studySiteOrganizations.contains(organizationRepository.findSingle(oQuery)));

	
	}

	private void createStudy(int number) {
		Study study = new Study();
		study.setDescription("StudyDesc" + number);
		study.setLongTitle("StudyDesc" + number);
		study.setShortTitle("StudyDesc" + number);
		study.setAssignedIdentifier("StudyIdentifier" + number);
		studyRepository.save(study);

	}

	private void createOrganization(int number) {
		Organization organization = new Organization();
		organization.setName("OrganizationName" + number);
		organization.setNciInstituteCode("OrganizationNCI" + number);
		organizationRepository.save(organization);
	}

	@Required
	public void setOrganizationRepository(
			OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
	}

	@Required
	public void setStudyOrganizationRepository(
			StudyOrganizationRepository studyOrganizationRepository) {
		this.studyOrganizationRepository = studyOrganizationRepository;
	}

	@Required
	public void setStudyRepository(StudyRepository studyRepository) {
		this.studyRepository = studyRepository;
	}

}
