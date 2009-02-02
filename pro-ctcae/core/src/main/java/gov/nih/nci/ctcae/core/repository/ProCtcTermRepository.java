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
    public Collection<ProCtcTerm> find(ProCtcTermQuery query) {
        Collection<ProCtcTerm> proCtcTerms = super.find(query);
        for (ProCtcTerm proCtcTerm : proCtcTerms) {
            intializeTerm(proCtcTerm);
        }

        return proCtcTerms;
    }

    @Override
    public ProCtcTerm findById(Integer id) {
        ProCtcTerm proCtcTerm = super.findById(id);
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
