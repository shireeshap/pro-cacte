package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;

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

	private CrfItemDisplayRule crfItemDisplayRule, anotherCrfItemDisplayRule;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		crf = Fixture.createCrf();
		proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
		assertNotNull(proCtc);

		proProCtcTerm = proCtcTermRepository.findAndInitializeTerm(new ProCtcTermQuery()).iterator().next();
		assertNotNull(proProCtcTerm);


		Collection<ProCtcQuestion> questions = proCtcQuestionRepository.find(new ProCtcQuestionQuery());
		proCtcQuestion = questions.iterator().next();

		crfItem = new CrfItem();
		crfItem.setCrf(crf);
		crfItem.setProCtcQuestion(proCtcQuestion);
		crfItem.setDisplayOrder(1);
		crfItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
		crfItem.setInstructions("instructions");
		crfItem.setResponseRequired(Boolean.TRUE);

		crf.addCrfItem(crfItem);

		crfItemDisplayRule = Fixture.createCrfItemDisplayRules(null, 1);
		anotherCrfItemDisplayRule = Fixture.createCrfItemDisplayRules(null, 2);

	}


	private CRF saveCrfItemWithDisplayRule() {
		crfItem.addCrfItemDisplayRules(crfItemDisplayRule);
		crf = crfRepository.save(crf);
		return crf;
	}

	public void testSaveCrfItem() {
		crf = crfRepository.save(crf);
		crfItem = crf.getCrfItems().iterator().next();

		assertNotNull(crfItem.getId());
		assertEquals(CrfItemAllignment.HORIZONTAL, crfItem.getCrfItemAllignment());
		assertEquals("instructions", crfItem.getInstructions());
		assertTrue(crfItem.getResponseRequired());

	}


	public void testAddCrfItemDisplayRuleInCreateCrfItem() {

		crf = saveCrfItemWithDisplayRule();
		crfItem = crf.getCrfItems().iterator().next();

		CrfItemDisplayRule savedCrfItemDisplayRule = crfItem.getCrfItemDisplayRules().iterator().next();
		assertNotNull(savedCrfItemDisplayRule.getId());
		assertEquals(crfItem, savedCrfItemDisplayRule.getCrfItem());
		assertEquals(ProCtcValidValue.class.getName(), savedCrfItemDisplayRule.getRequiredObjectClass());
		assertEquals(Integer.valueOf(1), savedCrfItemDisplayRule.getRequiredObjectId());
	}

	public void testAddCrfItemDisplayRuleInEditCrfItem() {

		crf = saveCrfItemWithDisplayRule();
		crfItem = crf.getCrfItems().iterator().next();
		assertNotNull(crfItem.getId());

		crfItem.addCrfItemDisplayRules(anotherCrfItemDisplayRule);
		crf = crfRepository.save(crf);
		crfItem = crf.getCrfItems().iterator().next();
		assertFalse("must save crf item display rule", crfItem.getCrfItemDisplayRules().isEmpty());
		assertEquals(Integer.valueOf(2), Integer.valueOf(crfItem.getCrfItemDisplayRules().size()));
		for (CrfItemDisplayRule savedCrfItemDisplayRule : crfItem.getCrfItemDisplayRules()) {
			assertNotNull(crfItemDisplayRule.getId());
			assertEquals(crfItem, savedCrfItemDisplayRule.getCrfItem());
		}

	}


	public void testDeleteCrfItemDisplayRule() {


		crf = saveCrfItemWithDisplayRule();
		crfItem = crf.getCrfItems().iterator().next();

		assertNotNull(crfItem.getId());
		Integer id = crfItem.getCrfItemDisplayRules().iterator().next().getId();
		assertNotNull(id);

		crfItem.removeCrfItemDisplayRulesByIds(String.valueOf(id));
		crf = crfRepository.save(crf);
		crfItem = crf.getCrfItems().iterator().next();
		assertNotNull(crfItem.getId());
		assertTrue("must remove crf item display rule", crfItem.getCrfItemDisplayRules().isEmpty());


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
