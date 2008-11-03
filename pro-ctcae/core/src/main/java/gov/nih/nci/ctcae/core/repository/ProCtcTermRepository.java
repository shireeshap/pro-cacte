package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermRepository extends
		AbstractRepository<ProCtcTerm, ProCtcTermQuery> {

	@Override
	protected Class<ProCtcTerm> getPersistableClass() {
		return ProCtcTerm.class;

	}

	@Override
	public void delete(ProCtcTerm t) {
		throw new UnsupportedOperationException(
				"Delete is not supported for ProCtcQuestion");
	}

	@Override
	public ProCtcTerm save(ProCtcTerm t) {
		throw new UnsupportedOperationException(
				"Save is not supported for ProCtcQuestion");
	}
}
