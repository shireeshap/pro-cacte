package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermIntegrationTest extends AbstractJpaIntegrationTestCase {

	private ProCtcTermRepository proCtcTermRepository;
	private ProCtcTerm proProCtcTerm;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
	}

	public void testSaveCtcTerm() {
		proProCtcTerm = new ProCtcTerm();
		try {
			proCtcTermRepository.save(proProCtcTerm);
			fail("Expecting UnsupportedOperationException: Save is not supported for ProCtcQuestion");
		} catch (UnsupportedOperationException e) {
		}
	}

	public void testFindById() {
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
				.find(proCtcTermQuery);
		ProCtcTerm firstProProCtcTerm = ctcTerms.iterator().next();

		proProCtcTerm = proCtcTermRepository.findById(firstProProCtcTerm.getId());
		assertEquals(proProCtcTerm.getCtepCode(), firstProProCtcTerm.getCtepCode());
		assertEquals(proProCtcTerm.getCtepTerm(), firstProProCtcTerm.getCtepTerm());
		assertEquals(proProCtcTerm.getSelect(), firstProProCtcTerm.getSelect());
		assertEquals(proProCtcTerm.getTerm(), firstProProCtcTerm.getTerm());
		assertEquals(proProCtcTerm, firstProProCtcTerm);
	}

	public void testFindByQuery() {

		int size = jdbcTemplate
				.queryForInt("select count(*) from PRO_CTC_TERMS");
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		proCtcTermQuery.setMaximumResults(size + 1000);
		Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
				.find(proCtcTermQuery);

		assertFalse(ctcTerms.isEmpty());
		assertEquals(size, ctcTerms.size());
	}

	@Required
	public void setCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}
}
