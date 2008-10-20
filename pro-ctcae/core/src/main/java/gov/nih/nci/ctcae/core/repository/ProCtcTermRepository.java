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

}
