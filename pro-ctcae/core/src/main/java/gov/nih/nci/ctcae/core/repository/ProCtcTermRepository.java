package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ProCtcTermRepository extends AbstractRepository<ProCtcTerm, ProCtcTermQuery> {

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

	public Collection<ProCtcTerm> findAndInitializeTerm(ProCtcTermQuery query) {
		Collection<ProCtcTerm> proCtcTerms = super.find(query);
		for (ProCtcTerm proCtcTerm : proCtcTerms) {
			intializeTerm(proCtcTerm);
		}

		return proCtcTerms;
	}

	@Override
	public Collection<ProCtcTerm> find(final ProCtcTermQuery query) {
		throw new UnsupportedOperationException(
			"find is not supported for ProCtcTerm. Use findAndInitializeTerm method");


	}

	public ProCtcTerm findAndInitializeTerm(Integer proCtcTermId) {

		ProCtcTerm proCtcTerm = super.findById(proCtcTermId);
		intializeTerm(proCtcTerm);


		return proCtcTerm;
	}

	private void intializeTerm(final ProCtcTerm proCtcTerm) {
		if (proCtcTerm != null) {
			for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
				proCtcQuestion.getQuestionText();
				for (ProCtcValidValue validValue : proCtcQuestion.getValidValues()) {
					validValue.getValue();
				}
			}
		} else {
			log.error("pro ctc term is null");
		}
	}
}
