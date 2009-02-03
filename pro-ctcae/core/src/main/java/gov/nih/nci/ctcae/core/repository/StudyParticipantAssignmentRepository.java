package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;

// TODO: Auto-generated Javadoc
/**
 * The Class StudyParticipantAssignmentRepository.
 * 
 * @author mehul
 */

public class StudyParticipantAssignmentRepository extends AbstractRepository<StudyParticipantAssignment, StudyParticipantAssignmentQuery> {

 /* (non-Javadoc)
  * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#getPersistableClass()
  */
 @Override
    protected Class<StudyParticipantAssignment> getPersistableClass() {
        return StudyParticipantAssignment.class;

    }


}