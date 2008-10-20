package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.ProCtcValidValueQuery;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcValidValueRepository extends
		AbstractRepository<ProCtcValidValue, ProCtcValidValueQuery> {

	@Override
	protected Class<ProCtcValidValue> getPersistableClass() {
		return ProCtcValidValue.class;

	}

}
