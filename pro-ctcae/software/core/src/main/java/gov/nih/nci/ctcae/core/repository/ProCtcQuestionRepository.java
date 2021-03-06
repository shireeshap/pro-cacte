package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

//
/**
 * The Class ProCtcQuestionRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuestionRepository implements Repository<ProCtcQuestion, ProCtcQuestionQuery> {

    private GenericRepository genericRepository;

    public ProCtcQuestion findById(Integer questionId) {
        ProCtcQuestion proCtcQuestion = genericRepository.findById(ProCtcQuestion.class, questionId);
        if (proCtcQuestion != null) {
            for (ProCtcValidValue validValue : proCtcQuestion.getValidValues()) {
                validValue.getValue(SupportedLanguageEnum.ENGLISH);
            }
        }
        return proCtcQuestion;


    }

    public ProCtcQuestion save(ProCtcQuestion proCtcQuestion) {
        return genericRepository.save(proCtcQuestion);


    }

    public void delete(ProCtcQuestion proCtcQuestion) {

        genericRepository.delete(proCtcQuestion);
    }

    public Collection<ProCtcQuestion> find(ProCtcQuestionQuery query) {
        return genericRepository.find(query);


    }

    public ProCtcQuestion findSingle(ProCtcQuestionQuery query) {
        return genericRepository.findSingle(query);


    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
