package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ProCtcTermRepository implements Repository<ProCtcTerm, ProCtcTermQuery> {

    private GenericRepository genericRepository;

    public ProCtcTerm findById(Integer id) {
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, id);
        intializeTerm(proCtcTerm);

        return proCtcTerm;


    }

    public ProCtcTerm save(ProCtcTerm proCtcTerm) {
        return genericRepository.save(proCtcTerm);


    }

    public void delete(ProCtcTerm t) {
        throw new CtcAeSystemException(
                "Delete is not supported for ProCtcQuestion");
    }


    public Collection<ProCtcTerm> find(final ProCtcTermQuery query) {
        Collection<ProCtcTerm> proCtcTerms = genericRepository.find(query);
        for (ProCtcTerm proCtcTerm : proCtcTerms) {
            intializeTerm(proCtcTerm);
        }

        return proCtcTerms;


    }

    public ProCtcTerm findSingle(ProCtcTermQuery query) {
        return genericRepository.findSingle(query);


    }

    public ProCtcTerm findProCtcTermBySymptom(String symptom) {
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        proCtcTermQuery.filterByTerm(symptom);
        return genericRepository.findSingle(proCtcTermQuery);
    }


    private void intializeTerm(final ProCtcTerm proCtcTerm) {
        if (proCtcTerm != null) {
            for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
                proCtcQuestion.getQuestionText();
                for (ProCtcValidValue validValue : proCtcQuestion.getValidValues()) {
                    validValue.getValue();
                }
            }
        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

}
