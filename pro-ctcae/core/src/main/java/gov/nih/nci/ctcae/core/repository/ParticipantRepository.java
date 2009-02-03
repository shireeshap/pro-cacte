package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;

// TODO: Auto-generated Javadoc
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


}
