package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;

import java.util.Collection;

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

    public Collection<ProCtcQuestion> findAll() {
        ProCtcQuestionQuery query = new ProCtcQuestionQuery();
        return super.find(query);


    }
}
