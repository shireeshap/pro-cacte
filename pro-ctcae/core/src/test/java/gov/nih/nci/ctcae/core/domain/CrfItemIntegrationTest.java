package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.CrfItemQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CrfItemIntegrationTest extends AbstractJpaIntegrationTestCase {

	public void setCrfItemRepository(CrfItemRepository crfItemRepository) {
		this.crfItemRepository = crfItemRepository;
	}

	private CrfItemRepository crfItemRepository;
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

		crf = new CRF();
		crf.setTitle("Cancer CRF");
		crf.setDescription("Case Report Form for Cancer Patients");
		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");
		crf = crfRepository.save(crf);

		proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
		assertNotNull(proCtc);

		proProCtcTerm = proCtcTermRepository.find(new ProCtcTermQuery()).iterator().next();
		assertNotNull(proProCtcTerm);


		proCtcQuestion = new ProCtcQuestion();
		proCtcQuestion.setQuestionText("How is the pain?");
		proCtcQuestion.setProCtcTerm(proProCtcTerm);
		proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.FREQUENCY);
		ProCtcValidValue proCtcValidValue = new ProCtcValidValue("High");
		proCtcValidValue.setValue(1);
		proCtcQuestion.addValidValue(proCtcValidValue);
		ProCtcValidValue proCtcValidValue1 = new ProCtcValidValue("Low");
		proCtcValidValue1.setValue(2);
		proCtcQuestion.addValidValue(proCtcValidValue1);
		ProCtcValidValue proCtcValidValue2 = new ProCtcValidValue("Severe");
		proCtcValidValue2.setValue(3);
		proCtcQuestion.addValidValue(proCtcValidValue2);
		ProCtcValidValue proCtcValidValue3 = new ProCtcValidValue("Very High");
		proCtcValidValue3.setValue(3);
		proCtcQuestion.addValidValue(proCtcValidValue3);

		proCtcQuestionRepository.save(proCtcQuestion);

		crfItem = new CrfItem();
		crfItem.setCrf(crf);
		crfItem.setProCtcQuestion(proCtcQuestion);
		crfItem.setDisplayOrder(1);
		crfItemRepository.save(crfItem);
		crfItemRepository.find(new CrfItemQuery());
	}

	public void testSaveCrfItem() {
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
		CrfItem anotherCrfItem = crfItemRepository.save(crfItem);

		assertNotNull(anotherCrfItem.getId());
		assertNotNull(crfItem.getId());
		assertEquals(CrfItemAllignment.HORIZONTAL, anotherCrfItem.getCrfItemAllignment());
		assertEquals("instructions", anotherCrfItem.getInstructions());
		assertTrue(anotherCrfItem.getResponseRequired());


	}

	public void testSavingNullCrfItem() {
		invalidCrfItem = new CrfItem();

		try {
			invalidCrfItem = crfItemRepository.save(invalidCrfItem);
			crfItemRepository.find(new CrfItemQuery());
			fail("Expected DataIntegrityViolationException because title, status and formVersion are null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullCRFCrfItem() {
		invalidCrfItem = new CrfItem();
		try {
			invalidCrfItem.setProCtcQuestion(proCtcQuestion);
			invalidCrfItem.setDisplayOrder(1);
			invalidCrfItem = crfItemRepository.save(invalidCrfItem);
			crfItemRepository.find(new CrfItemQuery());
			fail("Expected DataIntegrityViolationException because CRF is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullProCtcTermCrfItem() {
		invalidCrfItem = new CrfItem();
		try {
			invalidCrfItem.setCrf(crf);
			invalidCrfItem.setDisplayOrder(1);
			invalidCrfItem = crfItemRepository.save(invalidCrfItem);
			crfItemRepository.find(new CrfItemQuery());
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
			invalidCrfItem = crfItemRepository.save(invalidCrfItem);
			fail("Expected DataIntegrityViolationException because DisplayOrder is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testFindById() {

		CrfItem existingCrfItem = crfItemRepository.findById(crfItem.getId());
		assertEquals(crfItem.getDisplayOrder(), existingCrfItem
			.getDisplayOrder());
		assertEquals(crfItem.getProCtcQuestion(), existingCrfItem.getProCtcQuestion());
		assertEquals(crfItem.getCrf(), existingCrfItem.getCrf());
		assertEquals(crfItem, existingCrfItem);
	}

	public void testFindByQuery() {

		CrfItemQuery crfItemQuery = new CrfItemQuery();

		Collection<? extends CrfItem> crfItems = crfItemRepository
			.find(crfItemQuery);
		assertFalse(crfItems.isEmpty());
		int size = jdbcTemplate
			.queryForInt("select count(*) from CRF_ITEMS crfItem");
		assertEquals(size, crfItems.size());
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
