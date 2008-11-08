package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;

/**
 * @author mehul
 */

public class StudyParticipantAssignmentRepository extends AbstractRepository<StudyParticipantAssignment, StudyParticipantAssignmentQuery> {

 @Override
    protected Class<StudyParticipantAssignment> getPersistableClass() {
        return StudyParticipantAssignment.class;

    }


}