package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
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

public class StudyParticipantCrfScheduleAddedQuestionRepository implements Repository<StudyParticipantCrfScheduleAddedQuestion, Query> {
    private GenericRepository genericRepository;

    public StudyParticipantCrfScheduleAddedQuestion findById(Integer id) {
        return genericRepository.findById(StudyParticipantCrfScheduleAddedQuestion.class, id);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public StudyParticipantCrfScheduleAddedQuestion save(StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion) {
        return genericRepository.save(studyParticipantCrfScheduleAddedQuestion);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)

    public void delete(StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion) {

        genericRepository.delete(studyParticipantCrfScheduleAddedQuestion);
    }

    public Collection<StudyParticipantCrfScheduleAddedQuestion> find(Query query) {
        return null;


    }

    public StudyParticipantCrfScheduleAddedQuestion findSingle(Query query) {
        return null;


    }


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
