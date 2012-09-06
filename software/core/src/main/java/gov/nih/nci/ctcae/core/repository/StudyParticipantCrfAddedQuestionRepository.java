package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfAddedQuestion;
import gov.nih.nci.ctcae.core.query.Query;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vinay Kumar
 * @since Mar 11, 2009
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)

public class StudyParticipantCrfAddedQuestionRepository implements Repository<StudyParticipantCrfAddedQuestion, Query> {
    private GenericRepository genericRepository;

    public StudyParticipantCrfAddedQuestion findById(Integer id) {
        return genericRepository.findById(StudyParticipantCrfAddedQuestion.class, id);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)

    public StudyParticipantCrfAddedQuestion save(StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion) {
        return genericRepository.save(studyParticipantCrfAddedQuestion);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion) {

        genericRepository.delete(studyParticipantCrfAddedQuestion);
    }

    public Collection<StudyParticipantCrfAddedQuestion> find(Query query) {
        return null;


    }

    public StudyParticipantCrfAddedQuestion findSingle(Query query) {
        return null;


    }


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}