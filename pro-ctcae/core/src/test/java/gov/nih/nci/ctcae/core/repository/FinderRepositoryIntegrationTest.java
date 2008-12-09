package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Nov 18, 2008
 */
public class FinderRepositoryIntegrationTest extends AbstractHibernateIntegrationTestCase {

	private FinderRepository finderRepository;
	private CRF crf;
	private Study study;

	private StudyRepository studyRepository;
	private CRFRepository crfRepository;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		super.onSetUpInTransaction(); // To change body of overridden methods
		// use File | Settings | File Templates.
		login();

		study = createStudy();

		study = studyRepository.save(study);


		crf = new CRF();
		crf.setTitle("Cancer CRF");
		crf.setDescription("Case Report Form for Cancer Patients");
		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");
		crf = crfRepository.save(crf);
		study.addCrf(crf);
		study = studyRepository.save(study);


	}

	private Study createStudy() {
		Study study = new Study();
		study.setShortTitle("study short title");
		study.setLongTitle("study long title");
		study.setAssignedIdentifier("assigned identifier");
		return study;
	}

	public void testFindbyId() {
		ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, 1);
		assertNotNull(proCtcQuestion);
	}

	public void testFindAndInitializeProCtcQuestion() {
		ProCtcQuestion proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(1);
		assertNotNull(proCtcQuestion);
		assertFalse(proCtcQuestion.getValidValues().isEmpty());
		proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(-1001);
		assertNull(proCtcQuestion);
	}

	public void testFind() {
		ProCtcTermQuery query = new ProCtcTermQuery();
		List<? extends Persistable> list = finderRepository.find(query);
		assertNotNull(list);
		assertFalse(list.isEmpty());
	}

	public void testFindAndInitializeStudyCrf() {
		StudyCrf studyCrf = finderRepository.findAndInitializeStudyCrf(study.getStudyCrfs().get(0).getId());
		assertNotNull(studyCrf);
		studyCrf = finderRepository.findAndInitializeStudyCrf(-1001);
		assertNull(studyCrf);
	}

	public void setStudyRepository(final StudyRepository studyRepository) {
		this.studyRepository = studyRepository;
	}

	public void setCrfRepository(final CRFRepository crfRepository) {
		this.crfRepository = crfRepository;
	}

	public void setFinderRepository(final FinderRepository finderRepository) {
		this.finderRepository = finderRepository;
	}
}
