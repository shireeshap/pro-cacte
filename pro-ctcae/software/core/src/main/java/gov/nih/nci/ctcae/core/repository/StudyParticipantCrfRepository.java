package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.query.Query;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vinay Kumar
 * @since Mar 11, 2009
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)

public class StudyParticipantCrfRepository implements Repository<StudyParticipantCrf, Query> {
    private GenericRepository genericRepository;

    public StudyParticipantCrf findById(Integer id) {
        return genericRepository.findById(StudyParticipantCrf.class, id);


    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public StudyParticipantCrf save(StudyParticipantCrf studyParticipantCrf) {
        return genericRepository.save(studyParticipantCrf);


    }

    public void delete(StudyParticipantCrf studyParticipantCrf) {
     genericRepository.delete(studyParticipantCrf);

    }

    public Collection<StudyParticipantCrf> find(Query query) {
        return null;


    }

    public StudyParticipantCrf findSingle(Query query) {
        return null;


    }


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}