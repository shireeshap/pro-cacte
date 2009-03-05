package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

//
/**
 * The Class ParticipantRepository.
 *
 * @author mehul
 */

public class ParticipantRepository extends AbstractRepository<Participant, ParticipantQuery> {

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#getPersistableClass()
     */
    @Override
    protected Class<Participant> getPersistableClass() {
        return Participant.class;

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public Participant save(Participant participant) {
        Participant savedParticipant = super.save(participant);
        for(StudyParticipantAssignment studyParticipantAssignment : savedParticipant.getStudyParticipantAssignments()){
            studyParticipantAssignment.getStudyParticipantCrfs();
            studyParticipantAssignment.getStudyParticipantClinicalStaffs();
        }
        return savedParticipant;
    }
}
