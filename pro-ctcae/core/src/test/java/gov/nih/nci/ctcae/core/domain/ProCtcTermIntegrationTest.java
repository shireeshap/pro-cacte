package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermIntegrationTest extends AbstractHibernateIntegrationTestCase {

	private ProCtcTermRepository proCtcTermRepository;
	private ProCtcTerm proProCtcTerm;


	public void testSaveCtcTerm() {
		proProCtcTerm = new ProCtcTerm();
		try {
			proCtcTermRepository.save(proProCtcTerm);
			fail("Expecting UnsupportedOperationException: Save is not supported for ProCtcQuestion");
		} catch (UnsupportedOperationException e) {
		}
	}

	public void testFindNotSupported() {
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		try {
			proCtcTermRepository.find(proCtcTermQuery);
			fail("Expecting UnsupportedOperationException: find is not supported for ProCtcTerm. Use findAndInitializeTerm method");
		} catch (UnsupportedOperationException e) {
		}
	}

	public void testDeleteNotSupported() {
		try {
			proCtcTermRepository.delete(new ProCtcTerm());
			fail("Expecting UnsupportedOperationException: delete is not supported for ProCtcTerm.");
		} catch (UnsupportedOperationException e) {
		}
	}

	public void testFindAndInitialize() {
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
			.findAndInitializeTerm(proCtcTermQuery);
		ProCtcTerm firstProProCtcTerm = ctcTerms.iterator().next();

		proProCtcTerm = proCtcTermRepository.findById(firstProProCtcTerm.getId());
		assertEquals(proProCtcTerm.getCtepCode(), firstProProCtcTerm.getCtepCode());
		assertEquals(proProCtcTerm.getCtepTerm(), firstProProCtcTerm.getCtepTerm());
		assertEquals(proProCtcTerm.getSelect(), firstProProCtcTerm.getSelect());
		assertEquals(proProCtcTerm.getTerm(), firstProProCtcTerm.getTerm());
		assertEquals(proProCtcTerm, firstProProCtcTerm);
	}

	public void testFindAndInitializeById() {
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
			.findAndInitializeTerm(proCtcTermQuery);
		ProCtcTerm firstProProCtcTerm = ctcTerms.iterator().next();


		ProCtcTerm proCtcTerm = proCtcTermRepository.findAndInitializeTerm(firstProProCtcTerm.getId());
		assertEquals(proCtcTerm, firstProProCtcTerm);
	}

	public void testFilterByCtcTermHavingQuestionsOnly() {
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		proCtcTermQuery.filterByCtcTermHavingQuestionsOnly();
		Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
			.findAndInitializeTerm(proCtcTermQuery);

		assertFalse("must find atleast one ctc term", ctcTerms.isEmpty());
		for (ProCtcTerm proCtcTerm : ctcTerms) {
			assertFalse("must find atleast one question for each term", proCtcTerm.getProCtcQuestions().isEmpty());

		}
	}

	public void testFindByQuery() {

		int size = jdbcTemplate
			.queryForInt("select count(*) from PRO_CTC_TERMS");
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		proCtcTermQuery.setMaximumResults(size + 1000);
		Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
			.findAndInitializeTerm(proCtcTermQuery);

		assertFalse(ctcTerms.isEmpty());
		assertEquals(size, ctcTerms.size());
	}

	@Required
	public void setCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}
}
