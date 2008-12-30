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
	private CRFPage crfPage;
	private ProCtcQuestion proCtcQuestion;
	private ProCtc proCtc;
	private ProCtcTerm proProCtcTerm;
	private CrfPageItem crfPageItem, invalidCrfPageItem;

	private CrfItemDisplayRule crfItemDisplayRule, anotherCrfItemDisplayRule;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		crf = Fixture.createCrf();
		crfPage = new CRFPage();
		proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
		assertNotNull(proCtc);

		proProCtcTerm = proCtcTermRepository.findAndInitializeTerm(new ProCtcTermQuery()).iterator().next();
		assertNotNull(proProCtcTerm);


		Collection<ProCtcQuestion> questions = proCtcQuestionRepository.find(new ProCtcQuestionQuery());
		proCtcQuestion = questions.iterator().next();

		crfPageItem = new CrfPageItem();
		crfPageItem.setCrfPage(crfPage);
		crfPageItem.setProCtcQuestion(proCtcQuestion);
		crfPageItem.setDisplayOrder(1);
		crfPageItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
		crfPageItem.setInstructions("instructions");
		crfPageItem.setResponseRequired(Boolean.TRUE);

		crfPage.addCrfItem(crfPageItem);
		crf.addCrfPge(crfPage);

		crfItemDisplayRule = Fixture.createCrfItemDisplayRules(null, 1);
		anotherCrfItemDisplayRule = Fixture.createCrfItemDisplayRules(null, 2);

	}


	private CRF saveCrfItemWithDisplayRule() {
		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule);
		crf = crfRepository.save(crf);
		return crf;
	}

	public void testSaveCrfItem() {
		crf = crfRepository.save(crf);
		crfPageItem = crfPage.getCrfItemsSortedByDislayOrder().iterator().next();

		assertNotNull(crfPageItem.getId());
		assertEquals(CrfItemAllignment.HORIZONTAL, crfPageItem.getCrfItemAllignment());
		assertEquals("instructions", crfPageItem.getInstructions());
		assertTrue(crfPageItem.getResponseRequired());

	}


	public void testAddCrfItemDisplayRuleInCreateCrfItem() {

		crf = saveCrfItemWithDisplayRule();
		crfPageItem = crfPage.getCrfItemsSortedByDislayOrder().iterator().next();

		CrfItemDisplayRule savedCrfItemDisplayRule = crfPageItem.getCrfItemDisplayRules().iterator().next();
		assertNotNull(savedCrfItemDisplayRule.getId());
		assertEquals(crfPageItem, savedCrfItemDisplayRule.getCrfItem());
		assertEquals(ProCtcValidValue.class.getName(), savedCrfItemDisplayRule.getRequiredObjectClass());
		assertEquals(Integer.valueOf(1), savedCrfItemDisplayRule.getRequiredObjectId());
	}

	public void testAddCrfItemDisplayRuleInEditCrfItem() {

		crf = saveCrfItemWithDisplayRule();
		crfPageItem = crfPage.getCrfItemsSortedByDislayOrder().iterator().next();
		assertNotNull(crfPageItem.getId());

		crfPageItem.addCrfItemDisplayRules(anotherCrfItemDisplayRule);
		crf = crfRepository.save(crf);
		crfPageItem = crfPage.getCrfItemsSortedByDislayOrder().iterator().next();
		assertFalse("must save crf item display rule", crfPageItem.getCrfItemDisplayRules().isEmpty());
		assertEquals(Integer.valueOf(2), Integer.valueOf(crfPageItem.getCrfItemDisplayRules().size()));
		for (CrfItemDisplayRule savedCrfItemDisplayRule : crfPageItem.getCrfItemDisplayRules()) {
			assertNotNull(crfItemDisplayRule.getId());
			assertEquals(crfPageItem, savedCrfItemDisplayRule.getCrfItem());
		}

	}


	public void testDeleteCrfItemDisplayRule() {


		crf = saveCrfItemWithDisplayRule();
		crfPageItem = crfPage.getCrfItemsSortedByDislayOrder().iterator().next();

		assertNotNull(crfPageItem.getId());
		Integer id = crfPageItem.getCrfItemDisplayRules().iterator().next().getRequiredObjectId();
		assertNotNull(id);

		crfPageItem.removeCrfItemDisplayRulesByIds(String.valueOf(id));
		crf = crfRepository.save(crf);
		crfPageItem = crfPage.getCrfItemsSortedByDislayOrder().iterator().next();
		assertNotNull(crfPageItem.getId());
		assertTrue("must remove crf item display rule", crfPageItem.getCrfItemDisplayRules().isEmpty());


	}


	public void testSavingNullCrfItem() {
		invalidCrfPageItem = new CrfPageItem();
		crfPage.addCrfItem(invalidCrfPageItem);
		crf.addCrfPge(crfPage);
		try {
			crfRepository.save(crf);
			fail("Expected DataIntegrityViolationException because title, status and formVersion are null");
		} catch (DataIntegrityViolationException e) {
		}
	}


	public void testSavingNullProCtcTermCrfItem() {
		invalidCrfPageItem = new CrfPageItem();
		try {
			invalidCrfPageItem.setCrfPage(crfPage);
			invalidCrfPageItem.setDisplayOrder(1);
			crfPage.addCrfItem(invalidCrfPageItem);
			crf.addCrfPge(crfPage);
			crfRepository.save(crf);
			fail("Expected DataIntegrityViolationException because ProCtcQuestion is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullDisplayOrderCrfItem() {
		invalidCrfPageItem = new CrfPageItem();
		try {
			invalidCrfPageItem.setCrfPage(crfPage);
			invalidCrfPageItem.setDisplayOrder(null);
			invalidCrfPageItem.setProCtcQuestion(proCtcQuestion);
			crfPage.addCrfItem(invalidCrfPageItem);
			crf.addCrfPge(crfPage);

			crfRepository.save(crf);
			fail("Expected DataIntegrityViolationException because DisplayOrder is null");
		} catch (DataIntegrityViolationException e) {
		}
	}


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
