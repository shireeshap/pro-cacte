package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;

import java.util.Collection;

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

    public Collection<ProCtcTerm> findAll() {
        ProCtcTermQuery query= new ProCtcTermQuery();
        return super.find(query);


    }
}
