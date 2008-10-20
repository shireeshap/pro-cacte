package gov.nih.nci.ctcae.core.domain;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.query.CtcTermQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CtcTermIntegrationTest extends AbstractJpaIntegrationTestCase {

	private CtcTermRepository ctcTermRepository;
	private CtcTerm ctcTerm;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
	}

	public void testSaveCtcTerm() {
		ctcTerm = new CtcTerm();
		try {
			ctcTermRepository.save(ctcTerm);
			fail("Expecting UnsupportedOperationException: Save is not supported for CtcTerm");
		} catch (UnsupportedOperationException e) {
		}
	}

	public void testFindById() {
		CtcTermQuery ctcTermQuery = new CtcTermQuery();
		Collection<? extends CtcTerm> ctcTerms = ctcTermRepository
				.find(ctcTermQuery);
		CtcTerm firstCtcTerm = ctcTerms.iterator().next();

		ctcTerm = ctcTermRepository.findById(firstCtcTerm.getId());
		assertEquals(ctcTerm.getCtepCode(), firstCtcTerm.getCtepCode());
		assertEquals(ctcTerm.getCtepTerm(), firstCtcTerm.getCtepTerm());
		assertEquals(ctcTerm.getSelect(), firstCtcTerm.getSelect());
		assertEquals(ctcTerm.getTerm(), firstCtcTerm.getTerm());
		assertEquals(ctcTerm, firstCtcTerm);
	}

	public void testFindByQuery() {

		int size = jdbcTemplate
				.queryForInt("select count(*) from CTC_TERMS");
		CtcTermQuery ctcTermQuery = new CtcTermQuery();
		ctcTermQuery.setMaximumResults(size + 1000);
		Collection<? extends CtcTerm> ctcTerms = ctcTermRepository
				.find(ctcTermQuery);

		assertFalse(ctcTerms.isEmpty());
		assertEquals(size, ctcTerms.size());
	}

	@Required
	public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
		this.ctcTermRepository = ctcTermRepository;
	}
}
