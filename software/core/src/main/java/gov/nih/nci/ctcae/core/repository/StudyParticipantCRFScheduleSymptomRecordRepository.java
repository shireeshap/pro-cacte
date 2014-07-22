package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCRFScheduleSymptomRecord;
import gov.nih.nci.ctcae.core.query.StudyParticipantCRFScheduleSymptomRecordQuery;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IvrsScheduleRepository.
 *
 * @author Vinay Gangoli
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class StudyParticipantCRFScheduleSymptomRecordRepository implements Repository<StudyParticipantCRFScheduleSymptomRecord, StudyParticipantCRFScheduleSymptomRecordQuery> {
    
	private GenericRepository genericRepository;

    public StudyParticipantCRFScheduleSymptomRecord findById(Integer id) {
        return genericRepository.findById(StudyParticipantCRFScheduleSymptomRecord.class, id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public StudyParticipantCRFScheduleSymptomRecord save(StudyParticipantCRFScheduleSymptomRecord studyParticipantCRFScheduleSymptomRecord) {
        return genericRepository.save(studyParticipantCRFScheduleSymptomRecord);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(StudyParticipantCRFScheduleSymptomRecord studyParticipantCRFScheduleSymptomRecord) {
        genericRepository.delete(studyParticipantCRFScheduleSymptomRecord);
    }

    public List<StudyParticipantCRFScheduleSymptomRecord> find(StudyParticipantCRFScheduleSymptomRecordQuery studyParticipantCRFScheduleSymptomRecordQuery) {
    	return genericRepository.find(studyParticipantCRFScheduleSymptomRecordQuery);
    }

    public StudyParticipantCRFScheduleSymptomRecord findSingle(StudyParticipantCRFScheduleSymptomRecordQuery studyParticipantCRFScheduleSymptomRecordQuery) {
    	return genericRepository.findSingle(studyParticipantCRFScheduleSymptomRecordQuery);
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
    
}

