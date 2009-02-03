package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;

// TODO: Auto-generated Javadoc
/**
 * The Class ProCtcQuestionRepository.
 * 
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuestionRepository extends
        AbstractRepository<ProCtcQuestion, ProCtcQuestionQuery> {

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#getPersistableClass()
     */
    @Override
    protected Class<ProCtcQuestion> getPersistableClass() {
        return ProCtcQuestion.class;

    }


    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#findById(java.lang.Integer)
     */
    @Override
    public ProCtcQuestion findById(Integer questionId) {
        ProCtcQuestion proCtcQuestion = super.findById(questionId);
        if (proCtcQuestion != null) {
            for (ProCtcValidValue validValue : proCtcQuestion.getValidValues()) {
                validValue.getValue();
            }
        }
        return proCtcQuestion;


    }
}
