package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;

/**
 * @author mehul
 */

public class ParticipantRepository extends AbstractRepository<Participant, ParticipantQuery> {

    @Override
    protected Class<Participant> getPersistableClass() {
        return Participant.class;

    }


}
