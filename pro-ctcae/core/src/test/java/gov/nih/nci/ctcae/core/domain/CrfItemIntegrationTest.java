package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CrfItemIntegrationTest extends AbstractHibernateIntegrationTestCase {


	private CRFRepository crfRepository;
	private ProCtcRepository proCtcRepository;
	private ProCtcTermRepository proCtcTermRepository;
	private ProCtcQuestionRepository proCtcQuestionRepository;
	private CRF crf;
	private ProCtcQuestion proCtcQuestion;
	private ProCtc proCtc;
	private ProCtcTerm proProCtcTerm;
	private CrfItem crfItem, invalidCrfItem;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		createCrf();
		proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
		assertNotNull(proCtc);

		proProCtcTerm = proCtcTermRepository.findAndInitializeTerm(new ProCtcTermQuery()).iterator().next();
		assertNotNull(proProCtcTerm);


		proCtcQuestion = proCtcQuestionRepository.find(new ProCtcQuestionQuery()).iterator().next();


	}

	private void saveCrfItem() {

		crfItem = new CrfItem();
		crfItem.setCrf(crf);
		crfItem.setProCtcQuestion(proCtcQuestion);
		crfItem.setDisplayOrder(1);
		crf.addCrfItem(crfItem);
		crf = crfRepository.save(crf);
	}

	private void createCrf() {
		crf = new CRF();
		crf.setTitle("Cancer CRF");
		crf.setDescription("Case Report Form for Cancer Patients");
		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");
	}


	public void testSaveCrfItem() {
		saveCrfItem();
		assertNotNull(crfItem.getId());
	}

	public void testSaveCrfItemWithAdditionalProperties() {
		crfItem = new CrfItem();
		crfItem.setCrf(crf);
		crfItem.setProCtcQuestion(proCtcQuestion);
		crfItem.setDisplayOrder(1);
		crfItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
		crfItem.setInstructions("instructions");
		crfItem.setResponseRequired(Boolean.TRUE);

		crf.addCrfItem(crfItem);
		crf = crfRepository.save(crf);

		CrfItem anotherCrfItem = crf.getCrfItems().get(0);

		assertNotNull(anotherCrfItem.getId());
		assertNotNull(crfItem.getId());
		assertEquals(CrfItemAllignment.HORIZONTAL, anotherCrfItem.getCrfItemAllignment());
		assertEquals("instructions", anotherCrfItem.getInstructions());
		assertTrue(anotherCrfItem.getResponseRequired());


	}

	public void testSavingNullCrfItem() {
		invalidCrfItem = new CrfItem();
		crf.addCrfItem(invalidCrfItem);
		try {
			crfRepository.save(crf);
			fail("Expected DataIntegrityViolationException because title, status and formVersion are null");
		} catch (DataIntegrityViolationException e) {
		}
	}


	public void testSavingNullProCtcTermCrfItem() {
		invalidCrfItem = new CrfItem();
		try {
			invalidCrfItem.setCrf(crf);
			invalidCrfItem.setDisplayOrder(1);
			crf.addCrfItem(invalidCrfItem);
			crfRepository.save(crf);
			fail("Expected DataIntegrityViolationException because ProCtcQuestion is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullDisplayOrderCrfItem() {
		invalidCrfItem = new CrfItem();
		try {
			invalidCrfItem.setCrf(crf);
			invalidCrfItem.setDisplayOrder(null);
			invalidCrfItem.setProCtcQuestion(proCtcQuestion);
			crf.addCrfItem(invalidCrfItem);
			crfRepository.save(crf);
			fail("Expected DataIntegrityViolationException because DisplayOrder is null");
		} catch (DataIntegrityViolationException e) {
		}
	}


	@Required
	public void setCRFRepository(CRFRepository crfRepository) {
		this.crfRepository = crfRepository;
	}

	public void setProCtcTermRepository(
		ProCtcQuestionRepository proCtcQuestionRepository) {
		this.proCtcQuestionRepository = proCtcQuestionRepository;
	}

	public void setProCtcRepository(ProCtcRepository proCtcRepository) {
		this.proCtcRepository = proCtcRepository;
	}

	public void setCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}
}
