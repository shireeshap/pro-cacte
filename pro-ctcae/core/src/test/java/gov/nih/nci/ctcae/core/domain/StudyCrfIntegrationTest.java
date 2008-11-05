package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author Harsh Agarwal
 * @created Oct 16, 2008
 */
public class StudyCrfIntegrationTest extends AbstractJpaIntegrationTestCase {

	private StudyRepository studyRepository;
	private Study inValidStudy, studyWithStudyOrganizations;

	private StudySite nciStudySite;
	private Organization nci, duke;
	private OrganizationRepository organizationRepository;

	private StudyFundingSponsor studyFundingSponsor;
	private StudyCoordinatingCenter studyCoordinatingCenter;

	private CRF crf;
	private CRFRepository crfRepository;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction(); // To change body of overridden methods
		// use File | Settings | File Templates.
		login();
		nci = new Organization();
		nci.setName("National Cancer Institute");
		nci.setNciInstituteCode("NCI");
		nci = organizationRepository.save(nci);

		duke = new Organization();
		duke.setName("DUKE");
		duke.setNciInstituteCode("DUKE");
		duke = organizationRepository.save(duke);

		nciStudySite = new StudySite();
		nciStudySite.setOrganization(nci);

		studyFundingSponsor = new StudyFundingSponsor();
		studyFundingSponsor.setOrganization(nci);

		studyCoordinatingCenter = new StudyCoordinatingCenter();
		studyCoordinatingCenter.setOrganization(nci);

		studyWithStudyOrganizations = createStudy();
		studyWithStudyOrganizations.setStudyFundingSponsor(studyFundingSponsor);
		studyWithStudyOrganizations
				.setStudyCoordinatingCenter(studyCoordinatingCenter);
		studyWithStudyOrganizations.addStudySite(nciStudySite);

		studyWithStudyOrganizations = studyRepository
				.save(studyWithStudyOrganizations);

		assertNotNull(studyWithStudyOrganizations.getId());
		assertNotNull(studyWithStudyOrganizations.getId());
		assertEquals("must not create multiple study coordinating center",
				Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations
						.getStudyOrganizations().size()));
		assertEquals("must not create multiple funding sponsor", Integer
				.valueOf(3), Integer.valueOf(studyWithStudyOrganizations
				.getStudyOrganizations().size()));

		crf = new CRF();
		crf.setTitle("Cancer CRF");
		crf.setDescription("Case Report Form for Cancer Patients");
		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");
		crf = crfRepository.save(crf);

	}

	public void testAddCrfToStudy() {

		assertNotNull(crf);
		assertNotNull(studyWithStudyOrganizations);
		assertEquals(0, studyWithStudyOrganizations.getStudyCrfs().size());
		studyWithStudyOrganizations.addCrf(crf);
		assertEquals(1, studyWithStudyOrganizations.getStudyCrfs().size());

		studyRepository.save(studyWithStudyOrganizations);

		Study myStudy = studyRepository.findById(studyWithStudyOrganizations
				.getId());
		assertNotNull(myStudy.getStudyCrfs());
		assertEquals(1, myStudy.getStudyCrfs().size());

		CRF myCrf = myStudy.getStudyCrfs().get(0).getCrf();

		assertNotNull(myCrf);
		assertEquals(crf, myCrf);
	}

	public void testAddStudyCrfToStudy() {

		assertNotNull(crf);
		assertNotNull(studyWithStudyOrganizations);
		assertEquals(0, studyWithStudyOrganizations.getStudyCrfs().size());

		StudyCrf studyCrf = new StudyCrf();
		studyCrf.setCrf(crf);
		studyWithStudyOrganizations.addStudyCrf(studyCrf);

		assertEquals(1, studyWithStudyOrganizations.getStudyCrfs().size());

		studyRepository.save(studyWithStudyOrganizations);

		Study myStudy = studyRepository.findById(studyWithStudyOrganizations
				.getId());
		assertNotNull(myStudy.getStudyCrfs());
		assertEquals(1, myStudy.getStudyCrfs().size());

		CRF myCrf = myStudy.getStudyCrfs().get(0).getCrf();

		assertNotNull(myCrf);
		assertEquals(crf, myCrf);
	}

	public void testAddStudyToCrf() {

		assertNotNull(crf);
		assertNotNull(studyWithStudyOrganizations);
		assertEquals(0, crf.getStudyCrfs().size());
		crf.addStudy(studyWithStudyOrganizations);

		assertEquals(1, crf.getStudyCrfs().size());

		crfRepository.save(crf);

		CRF myCrf = crfRepository.findById(crf.getId());

		assertNotNull(myCrf.getStudyCrfs());
		assertEquals(1, myCrf.getStudyCrfs().size());

		Study myStudy = myCrf.getStudyCrfs().get(0).getStudy();

		assertNotNull(myStudy);
		assertEquals(studyWithStudyOrganizations, myStudy);
	}

	public void testAddStudyCrfToCrf() {

		assertNotNull(crf);
		assertNotNull(studyWithStudyOrganizations);
		assertEquals(0, crf.getStudyCrfs().size());

		StudyCrf studyCrf = new StudyCrf();
		studyCrf.setStudy(studyWithStudyOrganizations);
		crf.addStudyCrf(studyCrf);

		assertEquals(1, crf.getStudyCrfs().size());

		crfRepository.save(crf);

		CRF myCrf = crfRepository.findById(crf.getId());
		assertNotNull(crf.getStudyCrfs());
		assertEquals(1, crf.getStudyCrfs().size());

		Study myStudy = myCrf.getStudyCrfs().get(0).getStudy();

		assertNotNull(myStudy);
		assertEquals(myStudy, studyWithStudyOrganizations);
	}

	private Study createStudy() {
		Study study = new Study();
		study.setShortTitle("study short title");
		study.setLongTitle("study long title");
		study.setAssignedIdentifier("assigned identifier");
		return study;
	}

	@Required
	public void setOrganizationRepository(
			OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
	}

	@Required
	public void setStudyRepository(StudyRepository studyRepository) {
		this.studyRepository = studyRepository;
	}

	@Required
	public void setCrfRepository(CRFRepository crfRepository) {
		this.crfRepository = crfRepository;
	}
}
