package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.query.CtcTermQuery;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CtcTermRepository extends
		AbstractRepository<CtcTerm, CtcTermQuery> {

	@Override
	protected Class<CtcTerm> getPersistableClass() {
		return CtcTerm.class;

	}

	@Override
	public void delete(CtcTerm t) {
		throw new UnsupportedOperationException(
				"Delete is not supported for CtcTerm");
	}

	@Override
	public CtcTerm save(CtcTerm t) {
		throw new UnsupportedOperationException(
				"Save is not supported for CtcTerm");
	}
}
