package gov.nih.nci.ctcae.core.repository.secured;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.query.Query;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vinay Kumar
 * @since Mar 11, 2009
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class StudyParticipantCrfScheduleRepository implements Repository<StudyParticipantCrfSchedule, Query> {
    private GenericRepository genericRepository;

    public StudyParticipantCrfSchedule findById(Integer id) {
        return genericRepository.findById(StudyParticipantCrfSchedule.class, id);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public StudyParticipantCrfSchedule save(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        return genericRepository.save(studyParticipantCrfSchedule);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        genericRepository.delete(studyParticipantCrfSchedule);

    }

    public List<StudyParticipantCrfSchedule> find(Query query) {
        return genericRepository.find(query);
    }

    public StudyParticipantCrfSchedule findSingle(Query query) {
        return null;


    }

    public Long findWithCount(StudyParticipantCrfScheduleQuery query) {
        return genericRepository.findWithCount(query);
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
