package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuestionRepository extends
        AbstractRepository<ProCtcQuestion, ProCtcQuestionQuery> {

    @Override
    protected Class<ProCtcQuestion> getPersistableClass() {
        return ProCtcQuestion.class;

    }


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
